package ch.no1hardy.service.front.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetRes {
    private String id;
    private String gameId;
    private Integer scoreTeamHome;
    private Integer scoreTeamGuest;
    private Integer joker;
}
