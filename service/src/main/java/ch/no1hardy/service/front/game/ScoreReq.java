package ch.no1hardy.service.front.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreReq {
    private String game;
    private Integer scoreTeamHome;
    private Integer scoreTeamGuest;

    public boolean isValid() {
        return (getScoreTeamHome() == null || getScoreTeamHome() >= 0) &&
                (getScoreTeamGuest() == null || getScoreTeamGuest() >= 0);
    }
}
