package ch.no1hardy.service.front.group;

import ch.no1hardy.service.front.game.BetGameRes;

import java.time.LocalDateTime;
import java.util.List;

public record GroupRes(
        String id,
        String name,
        Double percentage,
        Double percentageResult,
        LocalDateTime lastSavedAt,
        LocalDateTime lastSavedAtResult,
        AvailableJokers availableJokers,
        List<BetGameRes> games,
        Boolean isKnockout
) {
    public GroupRes {
        if (games == null) {
            games = List.of();
        }
        if (isKnockout == null) {
            isKnockout = false;
        }
    }
}
