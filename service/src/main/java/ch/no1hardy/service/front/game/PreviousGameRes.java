package ch.no1hardy.service.front.game;

import ch.no1hardy.service.front.team.TeamRes;
import lombok.Data;

@Data
public class PreviousGameRes {
    private String id;
    private TeamRes teamHome;
    private TeamRes teamGuest;
    private ScoreRes result;
}
