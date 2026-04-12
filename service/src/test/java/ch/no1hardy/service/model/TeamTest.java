package ch.no1hardy.service.model;

import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamTest {
    Team team = new Team();

    @BeforeEach
    void beforeEach() {
        Game game1 = new Game();
        game1.setId("game-1");
        Game game2 = new Game();
        game2.setId("game-2");
        Game game3 = new Game();
        game3.setId("game-3");

        team.setFlag("flag-1");
        team.setName("Team 1");
        team.setId("team-1");
        team.setGamesHome(List.of(game1));
        team.setGamesGuest(List.of(game2, game3));
    }

    @Test
    void shouldReturnAllGamesOfTeam() {
        assertEquals(List.of("game-1", "game-2", "game-3"), team.getGames().stream().map(Game::getId).toList());
    }

    @Test
    void shouldAddGameToPreviousWithResultAndNotInKnockout() {
        team.setGamesGuest(List.of());
        Game game = createGame(false, true);
        team.setGamesHome(List.of(game));

        assertEquals(List.of(game), team.getPreviousGames());
    }

    @Test
    void shouldNotAddGameToPreviousWithResultAndInKnockout() {
        team.setGamesGuest(List.of());
        Game game = createGame(true, true);
        team.setGamesHome(List.of(game));

        assertEquals(List.of(), team.getPreviousGames());
    }

    @Test
    void shouldNotAddGameToPreviousWithoutResultAndNotInKnockout() {
        team.setGamesHome(List.of());
        Game game = createGame(false, false);
        team.setGamesGuest(List.of(game));

        assertEquals(List.of(), team.getPreviousGames());
    }

    @Test
    void shouldNotAddGameToPreviousWithoutResultAndInKnockout() {
        team.setGamesHome(List.of());
        Game game = createGame(true, false);
        team.setGamesGuest(List.of(game));

        assertEquals(List.of(), team.getPreviousGames());
    }

    private Game createGame(boolean isKnockout, boolean hasResult) {
        // Prepare group
        Group group = new Group();
        group.setIsKnockout(isKnockout);

        // Create game
        Game game = new Game();
        game.setGroup(group);
        if (hasResult) game.setResult(new Score());

        return game;
    }
}
