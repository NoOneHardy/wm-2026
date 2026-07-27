# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Beticon** is a betting game initially developed for the FIFA World Cup 2026, now maintained and updated for future events. It's a full-stack application with a Gradle-based monorepo structure consisting of a frontend (Angular) and backend (Spring Boot) service, along with supporting infrastructure (MySQL database, nginx proxy).

- **Live Demo**: https://beticon-dev.no1hardy.ch
- **Git Conventions**: See `docs/git-conventions.md` for binding conventions on commits, branches, and PRs

## Architecture

### Monorepo Structure
This is a **Gradle monorepo** with two main projects:
- `frontend/` - Angular 21 application (pnpm-managed)
- `service/` - Spring Boot 3.4 backend (Java 21)

Supporting services:
- `mysql/` - Database configuration
- `proxy/` - nginx reverse proxy

### Frontend Architecture (Angular 21)
The frontend follows **standalone component architecture** with feature modules:

**Core Feature Modules**:
- `admin/` - Admin panel (game/group/team/user/result management) + NgRx state management
- `bet-management/` - Bet placement and management
- `dashboard/` - User dashboard
- `game/` - Game information and details
- `leaderboard/` - Player rankings
- `home/` - Landing page
- `root/` - Root layout component
- `settings/` - User settings
- `user-management/` - User profile management
- `verification/` - Email verification flow
- `shared/` - Shared components, services, utilities
- `model/` - TypeScript interfaces and models

**State Management**: NgRx (@ngrx/store, @ngrx/effects) is used in the admin module for managing complex state.

**UI Framework**: Angular Material 21 with Material Design components.

**Styling**: Uses SCSS (theming.scss for Material theming, component-scoped styles).

**Component Naming**: Components use `bet-` prefix and kebab-case (e.g., `bet-dashboard`, `bet-statistics`).

### Backend Architecture (Spring Boot 3.4)
Java 21 backend using Spring Boot with:
- **Controllers**: Separate controller classes for each domain (GameController, UserController, AdminController, etc.)
- **Security**: Spring Security with JWT token authentication (JJWT library)
- **Database**: Spring Data JPA with MySQL
- **DTOs**: MapStruct for mapping between entities and DTOs
- **Configuration**: Global exception handling (ApiResponseAdvice), JWT filters, security configuration
- **Email**: Jakarta Mail for sending verification/reset emails
- **Utilities**: Helper classes for common operations (DateHelper, StringHelper, ListHelper)

## Development Setup

### Prerequisites
- Node.js and pnpm (for frontend)
- Java 21 (for backend)
- Gradle (wrapper included as ./gradlew)
- Docker & Docker Compose (for local services)
- MySQL (if running without Docker)

### Local Development

**Start all services (recommended)**:
```bash
docker-compose up
```

This brings up:
- Frontend dev server (http://localhost:4200)
- Backend service (http://localhost:8080)
- MySQL database (port 3306)
- nginx proxy (http://localhost:80)

### Frontend Commands

**Install dependencies**:
```bash
cd frontend
pnpm install
```

**Development server**:
```bash
pnpm start
# or
cd frontend && ng serve --host 0.0.0.0
```
Runs on http://localhost:4200

**Build for production**:
```bash
pnpm build
# or from monorepo root:
./gradlew :frontend:build
```

**Linting**:
```bash
pnpm lint           # Show lint issues
pnpm lint:fix       # Auto-fix lint issues
# or from monorepo root:
./gradlew :frontend:lint
```

**Testing** (uses Karma + Jasmine):
```bash
pnpm test                # Watch mode
pnpm test-ci             # Single run (headless Chrome)
# or from monorepo root:
./gradlew :frontend:test
```

**Watch mode** (compilation only):
```bash
pnpm watch
```

### Backend Commands

**Build**:
```bash
./gradlew :service:build
```

**Run tests**:
```bash
./gradlew :service:test
```

Tests use JUnit 5 and Spring Boot Test. For database tests, an embedded H2 database is used.

**Run development server**:
```bash
./gradlew :service:bootRun
```
Runs on http://localhost:8080 by default.

**Configuration**: Backend config is loaded from `.env` file (see `.env.example` for template).

## Testing

### Frontend Testing
- **Test Framework**: Karma + Jasmine
- **Run Tests**: `pnpm test` (watch) or `pnpm test-ci` (single run)
- **Coverage**: Karma coverage plugin configured
- **Component Tests**: Use `.spec.ts` files adjacent to components
- **Angular TestBed**: Use for component and service testing with DI

### Backend Testing
- **Test Framework**: JUnit 5 (JUnit Platform)
- **Run Tests**: `./gradlew :service:test`
- **Database Tests**: Use embedded H2 database instead of MySQL
- **Spring Test**: Use @SpringBootTest for integration tests

## Code Quality

### Frontend Linting
- **Linter**: ESLint with TypeScript and Angular plugins
- **Rules Enforced**:
  - 2-space indentation
  - Single quotes (template literals allowed)
  - No semicolons
  - Component selectors use `bet-` prefix in kebab-case
  - Warn on unused variables (except those prefixed with `_`)
  - Angular-specific rules (no input renaming, etc.)

**Configuration**: `frontend/eslint.config.js`

### Type Checking
- TypeScript 5.9.3 for strict type checking
- `tsconfig.json` configured for strict mode
- Run with IDE/editor integration or `ng build` will catch errors

## Build & Deployment

### CI/CD Pipeline
GitHub Actions workflows run on every push:

**Frontend** (`.github/workflows/frontend.yml`):
1. Lint check (via Gradle)
2. Unit tests (via Gradle)
3. Docker build (pushed to docker.no1hardy.ch only on develop branch)

**Backend** (`.github/workflows/service.yml`):
1. Unit and integration tests
2. Loads secrets for configuration
3. Docker build and push (only on develop branch)

### Docker Images
- Frontend Dockerfile: `frontend/Dockerfile` (nginx-based)
- Backend Dockerfile: `service/Dockerfile` (Java-based)

Both are pushed to private registry: `docker.no1hardy.ch/beticon/...`

## Key Dependencies

### Frontend
- **@angular/core@21.2.7** - Core framework
- **@ngrx/{store,effects}@21.1.0** - State management
- **@angular/material@21.2.5** - UI components
- **date-fns@4.1.0** - Date manipulation
- **rxjs@7.8.1** - Reactive programming

### Backend
- **spring-boot@3.4.1** - Web framework
- **spring-data-jpa** - Data access
- **spring-security@3.4** - Authentication/authorization
- **jjwt@0.11.5** - JWT token handling
- **mapstruct@1.5.3** - DTO mapping
- **lombok** - Boilerplate reduction
- **jakarta.mail@2.0.1** - Email functionality

## Important Patterns & Conventions

### Frontend
- **Standalone Components**: New components should be standalone where possible
- **Services**: Place business logic in services with @Injectable() decorator
- **State Management**: Admin module uses NgRx (actions, effects, reducers in `admin/store/`)
- **Models**: Define TypeScript interfaces in `shared/model/` or feature-specific `model/` folders
- **Shared Utilities**: Place reusable logic in `shared/` module
- **Lazy Loading**: Feature routes are lazy-loaded via `loadComponent` in app.routes.ts

### Backend
- **DTO Pattern**: Always use DTOs for API contracts (mapped from entities via MapStruct)
- **Service Layer**: Keep business logic in service classes, not controllers
- **Global Exception Handling**: ApiResponseAdvice handles standardized error responses
- **JWT Authentication**: Token validation via JwtAuthenticationFilter
- **Email Async**: Email notifications are sent asynchronously

## Environment & Configuration

### Frontend
- No `.env` needed for frontend (config in `app.config.ts`)
- API base URL is configured via HTTP interceptor

### Backend
- Requires `.env` file with secrets (copy from `.env.example`)
- Configuration loaded from application.properties (overridden by .env)
- **Key Settings**:
  - `jwt.secret` - Secret key for JWT signing
  - Database connection credentials
  - SMTP settings for email
  - `base.url` - Frontend URL for email links

## Debugging

### Frontend
- **Chrome DevTools**: Open with F12
- **Angular DevTools**: Chrome extension for debugging NgRx store
- **Logs**: Check browser console
- **Source Maps**: Enabled in development, disabled in production build

### Backend
- **IDE Debugging**: Use Gradle task with debug flag
- **Logs**: Check console output or configure logging in application.properties
- **Database**: Can inspect via MySQL CLI or tools

## Performance Considerations

### Frontend
- **Bundle Size Budgets** (enforced in CI):
  - Initial bundle: 500kB warning, 1MB error
  - Component styles: 2kB warning, 10kB error
- **Change Detection**: OnPush strategy recommended for components
- **Lazy Loading**: Feature modules are lazy-loaded

### Backend
- **Database Indexing**: MySQL schema indexed for common queries
- **Caching**: Consider cache headers in API responses
- **Connection Pooling**: Spring Boot manages connection pool automatically

## graphify

This project has a knowledge graph at graphify-out/ with god nodes, community structure, and cross-file relationships.

Rules:
- For codebase questions, first run `graphify query "<question>"` when graphify-out/graph.json exists. Use `graphify path "<A>" "<B>"` for relationships and `graphify explain "<concept>"` for focused concepts. These return a scoped subgraph, usually much smaller than GRAPH_REPORT.md or raw grep output.
- If graphify-out/wiki/index.md exists, use it for broad navigation instead of raw source browsing.
- Read graphify-out/GRAPH_REPORT.md only for broad architecture review or when query/path/explain do not surface enough context.
- After modifying code, run `graphify update .` to keep the graph current (AST-only, no API cost).
