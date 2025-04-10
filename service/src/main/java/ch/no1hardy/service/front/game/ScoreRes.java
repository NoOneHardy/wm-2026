package ch.no1hardy.service.front.game;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreRes {
    private String id;
    private String gameId;
    private Integer teamScoreHome;
    private Integer teamScoreGuest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
