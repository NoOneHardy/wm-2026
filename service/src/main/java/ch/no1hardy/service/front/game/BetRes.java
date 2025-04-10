package ch.no1hardy.service.front.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BetRes extends ScoreRes {
    private Integer joker;
}
