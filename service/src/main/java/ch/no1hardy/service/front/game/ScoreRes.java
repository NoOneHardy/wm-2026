package ch.no1hardy.service.front.game;

public record ScoreRes(
        String id,
        String gameId,
        Integer scoreTeamHome,
        Integer scoreTeamGuest
) {
}
