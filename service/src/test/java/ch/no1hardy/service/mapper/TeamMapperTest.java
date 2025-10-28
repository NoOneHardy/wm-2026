package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.game.PreviousGameRes;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.team.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeamMapperTest {
    @Autowired
    private TeamMapper teamMapper;

    @Test
    void shouldMapGameToPreviousGame() {
        Game game = new Game();
        game.setId("game-1");
        game.setTimestamp(LocalDateTime.of(2025, 5, 8, 20, 0));

        Team homeTeam = new Team();
        homeTeam.setId("team-home");
        game.setTeamHome(homeTeam);

        Team guestTeam = new Team();
        guestTeam.setId("team-guest");
        game.setTeamGuest(guestTeam);

        Group group = new Group();
        group.setId("group-1");
        group.setName("Gruppe 1");
        game.setGroup(group);

        Score result = new Score();
        result.setId("score-1");
        game.setResult(result);

        PreviousGameRes res = teamMapper.toPreviousGameDto(game);

        assertEquals("game-1", res.id());
        assertEquals("team-home", res.teamHome().id());
        assertEquals("team-guest", res.teamGuest().id());
        assertEquals("score-1", res.result().id());
        assertEquals("Gruppe 1", res.group());
    }
}
