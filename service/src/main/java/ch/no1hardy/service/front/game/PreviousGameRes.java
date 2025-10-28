package ch.no1hardy.service.front.game;

import ch.no1hardy.service.front.team.TeamRes;

public record PreviousGameRes(
        String id,
        TeamRes teamHome,
        TeamRes teamGuest,
        ScoreRes result,
        String group
) {
}
