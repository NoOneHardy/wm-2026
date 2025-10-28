package ch.no1hardy.service.front.game;

import ch.no1hardy.service.front.team.ExtendedTeamRes;

import java.time.LocalDateTime;

public record BetGameRes(
        String id,
        LocalDateTime timestamp,
        ExtendedTeamRes teamHome,
        ExtendedTeamRes teamGuest,
        ScoreRes result,
        BetRes bet,
        String groupId,
        String groupName
) {
}
