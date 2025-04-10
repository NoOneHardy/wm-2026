package ch.no1hardy.service.front.group;

import ch.no1hardy.service.front.game.BetGameRes;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupRes {
    private String id;
    private String name;
    private Double percentage;
    private LocalDateTime lastSavedAt;
    private List<BetGameRes> games;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
