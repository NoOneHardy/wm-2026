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
        boolean hasScores = getScoreTeamHome() != null && getScoreTeamGuest() != null;
        if (!hasScores) return false;

        return getScoreTeamHome() >= 0 && getScoreTeamGuest() >= 0;
    }
}
