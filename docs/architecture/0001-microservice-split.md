# 1. Target microservice architecture

## Status

Accepted

## Context

Beticon runs today as a single Spring Boot monolith. Three couplings inside it block any future
service extraction:

1. **`User.points` is written by the match domain but stored in the identity domain.**
   `GameService.uploadResult` → `UserService.addUserPoints` mutates `bet.getUser().setPoints(...)`
   with no transaction, relying on open-in-view dirty checking. This cannot survive a service
   boundary.
2. **`AuthService.getLoggedInUser()` is called from ~9 services and from inside MapStruct mappers**
   (`mapper/UserHelper` computes joker budgets during group mapping), so every service
   transitively needs the `User` entity.
3. **`GlobalDataFilter` + `ApiResponseAdvice` wrap every response** in
   `BaseResponse{data, globalData}`, where `globalData` is the top-5 unread notifications. No
   single service can produce that envelope after the split.

This ADR fixes the target architecture that Phase 0 (#360) and all later extraction phases design
towards, so every subsequent issue has a single reference instead of re-deriving it.

## Decision

### Granularity

Split into **4 services + a gateway**:

- `identity-service` — users, verification codes, auth
- `match-service` — teams, groups, games, match results, bets
- `scoring-service` — points, leaderboard
- `notification-service` — notifications, notification preferences, email dispatch
- `gateway` (Spring Cloud Gateway) — validates the JWT cookie, forwards `X-User-Id` /
  `X-User-Roles`, routes `/api/**` to the four services, hosts composition endpoints
  (`/dashboard`, `/home`) via WebClient fan-out

```
nginx proxy  ──┬─ /            → frontend
               ├─ /cdn/*       → cdn
               └─ /api/*       → gateway

gateway (Spring Cloud Gateway)
  · validates the jwt cookie, forwards X-User-Id / X-User-Roles
  · routes /api/** to the four services
  · hosts composition endpoints /dashboard and /home (WebClient fan-out)

identity-service      MySQL          User, VerificationCode
match-service         MySQL          Team, Group, Game, MatchResult, Bet
scoring-service       KurrentDB      PointsChanged event streams (one per user)
                      + Redis        leaderboard sorted set
notification-service  MongoDB        Notification, NotificationPreference, email dispatch

Redpanda topics
  identity  → user.registered, user.approved, user.deleted
  match     → match.result-uploaded (fat event: result + all bets), match.bet-placed
  scoring   → scoring.points-updated (published by the KurrentDB relay)
              scoring.ranking-changed
```

### Data isolation

Each service owns its database instance; nothing reads another service's tables directly. The
storage technology is chosen per service rather than defaulting to MySQL everywhere:

- **`identity-service` → MySQL.** Relational, low write volume, strong consistency requirements
  (unique email/username), no reason to deviate from the team's existing operational experience.
- **`match-service` → MySQL.** Same reasoning — relational data (teams/groups/games/bets) with
  foreign-key-shaped invariants that a relational store enforces cheaply.
- **`scoring-service` → KurrentDB + Redis.** Points are a derived, replayable value, not a source
  of truth to overwrite in place — the recurring bugs in the current model (#371, #372) are exactly
  what an event-sourced store eliminates by construction. KurrentDB holds one `PointsChanged` event
  stream per user as the append-only source of truth; a persistent subscription relay projects
  those events onto a Redis sorted set, which serves the leaderboard read path with O(log n)
  ranking instead of a full table scan/sort on every request.
- **`notification-service` → MongoDB.** Notifications are schema-loose, high-write, read-once
  documents with no relational joins to the rest of the domain — a document store fits the access
  pattern better than forcing a relational schema on it.

### Communication

- **Asynchronous, cross-service**: domain events over **Redpanda**, published via a
  **transactional outbox** in the owning service's own database so the event and the state change
  that produced it commit atomically. Consumers are independent and never call back synchronously
  into the producer.
- **Synchronous, cross-service**: REST, and only for **hydration** — read-only lookups like
  username/avatar resolution where an event-driven round trip would add latency for no benefit.
  Hydration calls never write.
- **Server → browser, for notifications**: **Server-Sent Events (SSE)**, not WebSocket. Notification
  delivery is one-directional (server pushes, the client never talks back on that channel), so SSE
  gets the real-time push without the cost WebSocket would add:
  - it rides plain HTTP through the gateway's existing request routing — no protocol upgrade
    handling to add to Spring Cloud Gateway;
  - it reconnects natively in the browser (`EventSource`), so the client doesn't need custom
    reconnect/backoff logic;
  - `notification-service` can scale horizontally behind the gateway the same way its REST
    endpoints do, without the sticky-session or Redis pub/sub backplane a multi-instance WebSocket
    deployment would need to fan events out to the right connection.

  WebSocket is revisited only if a future feature needs true bidirectional real-time traffic (e.g.
  live chat) — nothing in this epic does. This channel is also the replacement for the current
  `globalData` envelope (`GlobalDataFilter` / `ApiResponseAdvice`), which is removed in #381: the
  frontend stops receiving unread notifications piggybacked on every response and instead
  subscribes to a per-user SSE stream from `notification-service`.

### Leaderboard

Rebuilt on the **V2.0 event-sourced model** from the Affine *Leaderboard persistence model* doc:
KurrentDB event store → persistent subscription relay → Redpanda (`scoring.points-updated`,
`scoring.ranking-changed`) → Redis sorted set projection. The Redis key is parameterised as
`leaderboard:{tournamentId}` with a single constant tournament id for now, so multi-tournament
support later is additive rather than a rewrite of the event store.

### Migration strategy

**Strangler fig.** Services are extracted one at a time behind the gateway while the monolith keeps
serving everything not yet extracted; traffic for an extracted domain is cut over once its service
is live and verified. The **monolith is extracted last** — `scoring-service` (the piece most
coupled to the other two data domains via `User.points`) and the other decoupling work in Phase 0
happen first specifically so the later extractions are mechanical rather than architectural.

### Out of scope

- Multi-tournament (entity, scoping, UI) — the event schema and Redis key are shaped to allow it
  later, but nothing is built now.
- Squad membership.
- Distributed tracing.
- Kubernetes.

## Consequences

- Every mutating write path must become transactional and outbox-aware before extraction is safe
  (#370).
- No service outside the scoring package may read or write `User.points` after Phase 0 (#377).
- No mapper may resolve the current user; `AuthService.getLoggedInUser()` is replaced by a
  request-scoped `CurrentUser` (#374, #375).
- The `globalData` response envelope is dropped once the SSE notification channel replaces it
  (#381).
- Flyway must own the schema before any service boundary is drawn (#368).
