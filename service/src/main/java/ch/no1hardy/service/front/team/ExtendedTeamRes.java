package ch.no1hardy.service.front.team;

import ch.no1hardy.service.front.game.PreviousGameRes;

import java.util.List;

public record ExtendedTeamRes(
        String id,
        String name,
        String shortName,
        String flag,
        List<PreviousGameRes> previousGames
) {
}
