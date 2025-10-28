package ch.no1hardy.service.service;

import ch.no1hardy.service.GameHelper;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private GameRepository repository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId("user-1");
    }

    private Bet createBetForUser(User user, Game game) {
        Bet bet = new Bet();
        bet.setId("bet-1");
        bet.setUser(user);
        bet.setGame(game);
        return bet;
    }

    @Test
    @DisplayName("getUpcomingGames() - should return empty list when no games are available")
    void shouldReturnEmptyListWhenNoGamesAvailable() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));
        when(repository.findAll()).thenReturn(List.of());

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return list of upcoming games")
    void shouldReturnListOfOpenBetsForUser() {
        User otherUser = new User();
        otherUser.setId("other-user");

        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));
        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of(
                createBetForUser(otherUser, game1)
        ));
        game1.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of(
                createBetForUser(user, game2),
                createBetForUser(otherUser, game2)
        ));
        game2.setTimestamp(LocalDateTime.now().plusDays(3));
        when(repository.findAll()).thenReturn(List.of(game1, game2));

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(2, result.size());
        assertEquals("game-1", result.getFirst().id());
        assertEquals("game-2", result.get(1).id());
    }

    @Test
    @DisplayName("getUpcomingGames() - should filter games in the next 5 days")
    void shouldFilterGamesInNextFiveDays() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of());
        game1.setTimestamp(LocalDateTime.now().plusDays(5).minusMinutes(1));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of());
        game2.setTimestamp(LocalDateTime.now().plusDays(6));
        Game game3 = new Game();
        game3.setId("game-3");
        game3.setBets(List.of());
        game3.setTimestamp(LocalDateTime.now().minusDays(2));
        when(repository.findAll()).thenReturn(List.of(game1, game2, game3));

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().id());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return only active games")
    void shouldReturnOnlyActiveGames() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of());
        game1.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of());
        game2.setTimestamp(LocalDateTime.now().plusDays(2));
        game2.setDeletedAt(LocalDateTime.now());
        when(repository.findAll()).thenReturn(List.of(game1, game2));

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().id());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return games without a result")
    void shouldReturnGamesWithoutAResult() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of());
        game1.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of());
        Score score = new Score();
        score.setId("score-1");
        game2.setResult(score);
        game2.setTimestamp(LocalDateTime.now().plusDays(2));
        when(repository.findAll()).thenReturn(List.of(game1, game2));

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().id());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return up to 5 games")
    void shouldReturnUpToFiveGames() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of());
        game1.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of());
        game2.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game3 = new Game();
        game3.setId("game-3");
        game3.setBets(List.of());
        game3.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game4 = new Game();
        game4.setId("game-4");
        game4.setBets(List.of());
        game4.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game5 = new Game();
        game5.setId("game-5");
        game5.setBets(List.of());
        game5.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game6 = new Game();
        game6.setId("game-6");
        game6.setBets(List.of());
        game6.setTimestamp(LocalDateTime.now().plusDays(2));
        when(repository.findAll()).thenReturn(List.of(game1, game2, game3, game4, game5, game6));

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(5, result.size());
        assertEquals("game-1", result.getFirst().id());
        assertEquals("game-2", result.get(1).id());
        assertEquals("game-3", result.get(2).id());
        assertEquals("game-4", result.get(3).id());
        assertEquals("game-5", result.get(4).id());
    }

    @Test
    @DisplayName("getRecentResults() - should return empty list when no games are available")
    void recentResultsNoGames() {
        when(repository.findAll()).thenReturn(List.of());
        List<BetGameRes> result = service.getRecentResults();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getRecentResults() - should return only active games with results")
    void shouldReturnOnlyActiveGamesWithResults() {
        Game game1 = GameHelper.createGame("game-1");
        GameHelper.createScore("score-1", game1, 3, 0);

        Game game2 = GameHelper.createGame("game-2");
        game2.setDeletedAt(LocalDateTime.now());
        GameHelper.createScore("score-1", game2, 2, 1);

        Game game3 = GameHelper.createGame("game-3");

        when(repository.findAll()).thenReturn(List.of(game1, game2, game3));
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        List<BetGameRes> result = service.getRecentResults();

        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().id());
    }

    @Test
    @DisplayName("getRecentResults() - should return games sorted by result update time in descending order")
    void shouldReturnGamesSortedByResultUpdateTimeDescending() {
        Game game1 = GameHelper.createGame("game-1");
        Score score1 = GameHelper.createScore("score-1", game1, 2, 1);
        score1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        Game game2 = GameHelper.createGame("game-2");
        Score score2 = GameHelper.createScore("score-2", game2, 1, 1);
        score2.setUpdatedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(game1, game2));
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        List<BetGameRes> result = service.getRecentResults();

        assertEquals(2, result.size());
        assertEquals("game-2", result.get(0).id());
        assertEquals("game-1", result.get(1).id());
    }

    @Test
    @DisplayName("getRecentResults() - should limit results to 5 games")
    void shouldLimitResultsToFiveGames() {
        List<Game> games = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Game game = GameHelper.createGame("game-" + i);
            Score score = GameHelper.createScore("score-" + i, game, i, i + 1);
            score.setUpdatedAt(LocalDateTime.now().minusDays(i));
            games.add(game);
        }

        when(repository.findAll()).thenReturn(games);

        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));
        List<BetGameRes> result = service.getRecentResults();

        assertEquals(5, result.size());
        assertEquals("game-1", result.get(0).id());
        assertEquals("game-5", result.get(4).id());
    }

    @Test
    @DisplayName("getRecentResults() - should return recent results")
    void shouldReturnRecentResults() {
        when(userService.getCurrentUserRaw()).thenReturn(Optional.of(user));

        Game game1 = GameHelper.createGame("game-1");
        Score score1 = GameHelper.createScore("score-1", game1, 2, 1);
        score1.setUpdatedAt(LocalDateTime.now().minusDays(1));
        GameHelper.createBet("bet-1", user, game1, 2, 1);

        Game game2 = GameHelper.createGame("game-2");
        Score score2 = new Score();
        score2.setUpdatedAt(LocalDateTime.now());
        game2.setResult(score2);

        when(repository.findAll()).thenReturn(List.of(game1, game2));

        List<BetGameRes> result = service.getRecentResults();

        assertEquals(2, result.size());
        assertEquals("game-2", result.get(0).id());
        assertEquals("game-1", result.get(1).id());
        assertEquals("bet-1", result.get(1).bet().id());
    }
}
