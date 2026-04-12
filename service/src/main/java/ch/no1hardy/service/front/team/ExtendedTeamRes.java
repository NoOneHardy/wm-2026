package ch.no1hardy.service.front.team;

import ch.no1hardy.service.front.game.PreviousGameRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExtendedTeamRes extends TeamRes {
    private List<PreviousGameRes> previousGames;
}
