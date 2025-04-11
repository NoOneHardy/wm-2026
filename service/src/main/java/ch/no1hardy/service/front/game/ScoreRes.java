package ch.no1hardy.service.front.game;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScoreRes {
    private String id;
    private String gameId;
    private Integer scoreTeamHome;
    private Integer scoreTeamGuest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
