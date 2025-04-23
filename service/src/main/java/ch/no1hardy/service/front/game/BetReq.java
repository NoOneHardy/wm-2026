package ch.no1hardy.service.front.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetReq {
    private String game;
    private Integer scoreTeamHome;
    private Integer scoreTeamGuest;
    private Integer joker;
    private String user;

    public void clampJoker() {
        if (getJoker() > 3 || getJoker() < 1) {
            setJoker(1);
        }
    }

    public boolean isValid() {
        boolean hasScores = getScoreTeamHome() != null && getScoreTeamGuest() != null;
        boolean hasJoker = getJoker() != null;
        return hasScores && hasJoker;
    }
}
