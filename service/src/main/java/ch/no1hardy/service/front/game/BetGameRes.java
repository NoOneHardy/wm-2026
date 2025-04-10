package ch.no1hardy.service.front.game;

import ch.no1hardy.service.front.team.TeamRes;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BetGameRes {
    private String id;
    private LocalDateTime timestamp;
    private TeamRes teamHome;
    private TeamRes teamGuest;
    private ScoreRes result;
    private BetRes bet;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
