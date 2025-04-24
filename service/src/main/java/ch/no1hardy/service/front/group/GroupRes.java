package ch.no1hardy.service.front.group;

import ch.no1hardy.service.front.game.BetGameRes;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GroupRes {
    private String id;
    private String name;
    private Double percentage;
    private Double percentageResult;
    private LocalDateTime lastSavedAt;
    private LocalDateTime lastSavedAtResult;
    private Integer availableDoubleJokers;
    @Builder.Default
    private List<BetGameRes> games = List.of();
    @Builder.Default
    private Boolean isKnockout = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
