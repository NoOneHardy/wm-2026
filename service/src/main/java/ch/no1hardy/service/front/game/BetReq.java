package ch.no1hardy.service.front.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BetReq extends ScoreReq {
    private Integer joker;
    private String user;

    @Override
    public void clamp() {
        super.clamp();
        if (getJoker() > 3 || getJoker() < 1) {
            setJoker(1);
        }
    }

    public boolean isValid() {
        return super.isValid() && getJoker() != null;
    }
}
