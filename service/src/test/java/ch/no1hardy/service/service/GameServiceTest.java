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
    private AuthService authService;

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
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(repository.findAll()).thenReturn(List.of());

        List<BetGameRes> result = service.getUpcomingGames();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return list of upcoming games")
    void shouldReturnListOfOpenBetsForUser() {
        User otherUser = new User();
        otherUser.setId("other-user");

        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));
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
        assertEquals("game-1", result.getFirst().getId());
        assertEquals("game-2", result.get(1).getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should filter games in the next 5 days")
    void shouldFilterGamesInNextFiveDays() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

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
        assertEquals("game-1", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return only active games")
    void shouldReturnOnlyActiveGames() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

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
        assertEquals("game-1", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return games without a result")
    void shouldReturnGamesWithoutAResult() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

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
        assertEquals("game-1", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return up to 5 games")
    void shouldReturnUpToFiveGames() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

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
        assertEquals("game-1", result.getFirst().getId());
        assertEquals("game-2", result.get(1).getId());
        assertEquals("game-3", result.get(2).getId());
        assertEquals("game-4", result.get(3).getId());
        assertEquals("game-5", result.get(4).getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return games sorted by timestamp ascending")
    void shouldReturnGamesSortedByTimestampAscending() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of());
        game1.setTimestamp(LocalDateTime.now().plusDays(3));

        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of());
        game2.setTimestamp(LocalDateTime.now().plusDays(1));

        Game game3 = new Game();
        game3.setId("game-3");
        game3.setBets(List.of());
        game3.setTimestamp(LocalDateTime.now().plusDays(2));

        when(repository.findAll()).thenReturn(List.of(game1, game2, game3));

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(3, result.size());
        assertEquals("game-2", result.get(0).getId());
        assertEquals("game-3", result.get(1).getId());
        assertEquals("game-1", result.get(2).getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should exclude games that have already started")
    void shouldExcludeGamesThatHaveAlreadyStarted() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        Game pastGame = new Game();
        pastGame.setId("past-game");
        pastGame.setBets(List.of());
        pastGame.setTimestamp(LocalDateTime.now().minusMinutes(1));

        Game futureGame = new Game();
        futureGame.setId("future-game");
        futureGame.setBets(List.of());
        futureGame.setTimestamp(LocalDateTime.now().plusDays(1));

        when(repository.findAll()).thenReturn(List.of(pastGame, futureGame));

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(1, result.size());
        assertEquals("future-game", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should exclude game starting exactly at the 5-day boundary")
    void shouldExcludeGameAtExactlyFiveDaysBoundary() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        Game boundaryGame = new Game();
        boundaryGame.setId("boundary-game");
        boundaryGame.setBets(List.of());
        boundaryGame.setTimestamp(LocalDateTime.now().plusDays(5));

        Game justBeforeGame = new Game();
        justBeforeGame.setId("just-before-game");
        justBeforeGame.setBets(List.of());
        justBeforeGame.setTimestamp(LocalDateTime.now().plusDays(5).minusMinutes(1));

        when(repository.findAll()).thenReturn(List.of(boundaryGame, justBeforeGame));

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(1, result.size());
        assertEquals("just-before-game", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should include game whose score is soft-deleted (hasResult is false)")
    void shouldIncludeGameWithDeletedScore() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        Score deletedScore = new Score();
        deletedScore.setId("deleted-score");
        deletedScore.setDeletedAt(LocalDateTime.now());

        Game game = new Game();
        game.setId("game-with-deleted-score");
        game.setBets(List.of());
        game.setTimestamp(LocalDateTime.now().plusDays(2));
        game.setResult(deletedScore);

        when(repository.findAll()).thenReturn(List.of(game));

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(1, result.size());
        assertEquals("game-with-deleted-score", result.getFirst().getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should return the 5 games with the earliest timestamps when more qualify")
    void shouldReturnEarliestFiveGamesWhenMoreThanFiveQualify() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        List<Game> games = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Game game = new Game();
            game.setId("game-" + i);
            game.setBets(List.of());
            game.setTimestamp(LocalDateTime.now().plusHours(i));
            games.add(game);
        }

        when(repository.findAll()).thenReturn(games);

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(5, result.size());
        assertEquals("game-1", result.get(0).getId());
        assertEquals("game-2", result.get(1).getId());
        assertEquals("game-3", result.get(2).getId());
        assertEquals("game-4", result.get(3).getId());
        assertEquals("game-5", result.get(4).getId());
    }

    @Test
    @DisplayName("getUpcomingGames() - should apply all filters simultaneously")
    void shouldApplyAllFiltersSimultaneously() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        Game validGame = new Game();
        validGame.setId("valid-game");
        validGame.setBets(List.of());
        validGame.setTimestamp(LocalDateTime.now().plusDays(1));

        Game pastGame = new Game();
        pastGame.setId("past-game");
        pastGame.setBets(List.of());
        pastGame.setTimestamp(LocalDateTime.now().minusHours(2));

        Game farFutureGame = new Game();
        farFutureGame.setId("far-future-game");
        farFutureGame.setBets(List.of());
        farFutureGame.setTimestamp(LocalDateTime.now().plusDays(6));

        Score activeScore = new Score();
        activeScore.setId("score-1");
        Game gameWithResult = new Game();
        gameWithResult.setId("game-with-result");
        gameWithResult.setBets(List.of());
        gameWithResult.setTimestamp(LocalDateTime.now().plusDays(2));
        gameWithResult.setResult(activeScore);

        Game deletedGame = new Game();
        deletedGame.setId("deleted-game");
        deletedGame.setBets(List.of());
        deletedGame.setTimestamp(LocalDateTime.now().plusDays(1));
        deletedGame.setDeletedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(validGame, pastGame, farFutureGame, gameWithResult, deletedGame));

        List<BetGameRes> result = service.getUpcomingGames();

        assertEquals(1, result.size());
        assertEquals("valid-game", result.getFirst().getId());
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
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        List<BetGameRes> result = service.getRecentResults();

        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().getId());
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
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        List<BetGameRes> result = service.getRecentResults();

        assertEquals(2, result.size());
        assertEquals("game-2", result.get(0).getId());
        assertEquals("game-1", result.get(1).getId());
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

        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));
        List<BetGameRes> result = service.getRecentResults();

        assertEquals(5, result.size());
        assertEquals("game-1", result.get(0).getId());
        assertEquals("game-5", result.get(4).getId());
    }

    @Test
    @DisplayName("getRecentResults() - should return recent results")
    void shouldReturnRecentResults() {
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

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
        assertEquals("game-2", result.get(0).getId());
        assertEquals("game-1", result.get(1).getId());
        assertEquals("bet-1", result.get(1).getBet().getId());
    }
}
