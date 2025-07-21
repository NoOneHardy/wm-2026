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
        return (getScoreTeamHome() == null || getScoreTeamHome() >= 0) &&
                (getScoreTeamGuest() == null || getScoreTeamGuest() >= 0) &&
                getJoker() != null;
    }
}
