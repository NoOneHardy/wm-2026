package ch.no1hardy.service.front.game;

public record BetRes(
        String id,
        String gameId,
        Integer scoreTeamHome,
        Integer scoreTeamGuest,
        Integer joker
) {
}
