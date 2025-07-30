package ch.no1hardy.service.service;

import ch.no1hardy.service.GameHelper;
import ch.no1hardy.service.SecurityHelper;
import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    private DashboardService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LeaderboardService leaderboardService;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private UserRepository repository;

    @MockitoBean
    private GameService gameService;

    @Test
    @DisplayName("getGlobalStatistics() - should return zero statistics if no users exist")
    void shouldReturnZeroStatisticsIfNoUsersExist() {
        when(userService.listRaw()).thenReturn(List.of());

        GlobalStatistics globalStats = service.getGlobalStatistics();

        assertEquals(0, globalStats.getTotalPoints());
        assertEquals(0, globalStats.getCorrectGames());
        assertEquals(0, globalStats.getJokersWasted());
    }

    @Test
    @DisplayName("getOverallPercentage() - should return 0.0 if there are no games")
    void shouldReturnZeroIfNoGames() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Group group = new Group();
        group.setGames(List.of());
        when(groupService.listRaw()).thenReturn(List.of(group));

        assertEquals(0.0, service.getOverallPercentage(user));

        when(groupService.listRaw()).thenReturn(List.of());
        assertEquals(0.0, service.getOverallPercentage(user));
    }

    @Test
    @DisplayName("getOverallPercentage() - should return 0.0 if user has no bets")
    void shouldReturnZeroIfUserHasNoBets() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Group group = new Group();
        group.setGames(List.of(
                new Game(),
                new Game(),
                new Game(),
                new Game()
        ));
        when(groupService.listRaw()).thenReturn(List.of(group));

        assertEquals(0.0, service.getOverallPercentage(user));
    }

    @Test
    @DisplayName("getOverallPercentage() - should return correct percentage")
    void shouldReturnCorrectPercentage() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of(
                new Bet(),
                new Bet(),
                new Bet()
        ));

        Group group = new Group();
        group.setGames(List.of(
                new Game(),
                new Game(),
                new Game(),
                new Game()
        ));
        when(groupService.listRaw()).thenReturn(List.of(group));

        Double percentage = service.getOverallPercentage(user);
        assertEquals(75.0, percentage);
    }

    @Test
    @DisplayName("getUserSummary() - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFound() {
        SecurityHelper.mockNoLogin();
        assertThrows(BadRequestException.class, service::getUserSummary);
    }

    @Test
    @DisplayName("getStatistics() - should throw BadRequestException if no user is logged in")
    void shouldThrowBadRequestExceptionIfNoUserLoggedInForStatistics() {
        SecurityHelper.mockNoLogin();
        assertThrows(BadRequestException.class, service::getStatistics);
    }

    @Test
    @DisplayName("getStatistics(User) - should return correct statistics for a user with active bets")
    void shouldReturnCorrectStatisticsForUserWithActiveBets() {
        User user = new User();
        user.setId("user-1");

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        GameHelper.createBet("bet-1", user, game1, 2, 3);
        GameHelper.createBet("bet-2", user, game2, 1, 1, 2);

        GameHelper.createScore("score-1", game1, 2, 3);
        GameHelper.createScore("score-2", game2, 0, 4);

        Statistics stats = service.getStatistics(user);

        assertEquals(7, stats.getTotalGoalsBet());
        assertEquals(1, stats.getCorrectGames());
        assertEquals(1, stats.getJokersWasted());
    }

    @Test
    @DisplayName("getStatistics(User) - should return zero statistics for a user with no active bets")
    void shouldReturnZeroStatisticsForUserWithNoActiveBets() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Statistics stats = service.getStatistics(user);

        assertEquals(0, stats.getTotalGoalsBet());
        assertEquals(0, stats.getCorrectGames());
        assertEquals(0, stats.getJokersWasted());
    }

    @Test
    @DisplayName("getStatistics(User) - should return zero statistics for a user with inactive bets")
    void shouldReturnZeroStatisticsForUserWithInactiveBets() {
        User user = new User();
        user.setId("user-1");

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        Bet bet1 = GameHelper.createBet("bet-1", user, game1, 2, 3);
        bet1.setDeletedAt(LocalDateTime.now());
        Bet bet2 = GameHelper.createBet("bet-2", user, game2, 1, 1);
        bet2.setDeletedAt(LocalDateTime.now());

        Statistics stats = service.getStatistics(user);

        assertEquals(0, stats.getTotalGoalsBet());
        assertEquals(0, stats.getCorrectGames());
        assertEquals(0, stats.getJokersWasted());
    }

    @Test
    @DisplayName("getStatistics(User) - should handle games without result gracefully")
    void shouldHandleGamesWithoutResultGracefully() {
        User user = new User();
        user.setId("user-1");

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        GameHelper.createBet("bet-1", user, game1, 2, 3);
        GameHelper.createBet("bet-2", user, game2, 1, 1);

        Statistics stats = service.getStatistics(user);

        assertEquals(7, stats.getTotalGoalsBet());
        assertEquals(0, stats.getCorrectGames());
        assertEquals(0, stats.getJokersWasted());
    }

    @Test
    @DisplayName("getTotalGoalsBet(User) - should return total goals for active bets")
    void shouldReturnTotalGoalsForActiveBets() {
        User user = new User();

        GameHelper.createBet("bet-1", user, GameHelper.createGame("game-1"), 2, 3);
        GameHelper.createBet("bet-2", user, GameHelper.createGame("game-2"), 1, 1);

        int totalGoals = service.getTotalGoalsBet(user);

        assertEquals(7, totalGoals);
    }

    @Test
    @DisplayName("getTotalGoalsBet(User) - should return zero if user has no bets")
    void shouldReturnZeroIfUserHasNoBetsInGetTotalGoalsBet() {
        User user = new User();
        user.setBets(List.of());

        int totalGoals = service.getTotalGoalsBet(user);

        assertEquals(0, totalGoals);
    }

    @Test
    @DisplayName("getTotalGoalsBet(User) - should return zero if all bets are inactive")
    void shouldReturnZeroIfAllBetsAreInactive() {
        User user = new User();

        Bet bet1 = GameHelper.createBet("bet-1", user, GameHelper.createGame("game-1"), 2, 3);
        bet1.setDeletedAt(LocalDateTime.now());
        Bet bet2 = GameHelper.createBet("bet-2", user, GameHelper.createGame("game-2"), 1, 1);
        bet2.setDeletedAt(LocalDateTime.now());

        int totalGoals = service.getTotalGoalsBet(user);

        assertEquals(0, totalGoals);
    }

    @Test
    @DisplayName("getCorrectGames(User) - should return correct count for active bets with exact score matches")
    void shouldReturnCorrectCountForActiveBetsWithExactScoreMatches() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        GameHelper.createBet("bet-1", user, game1, 2, 3);
        GameHelper.createBet("bet-2", user, game2, 1, 1);

        GameHelper.createScore("score-1", game1, 2, 3);
        GameHelper.createScore("score-2", game2, 1, 1);

        int correctGames = service.getCorrectGames(user);

        assertEquals(2, correctGames);
    }

    @Test
    @DisplayName("getCorrectGames(User) - should return zero if no bets match exact scores")
    void shouldReturnZeroIfNoBetsMatchExactScores() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        GameHelper.createBet("bet-1", user, game1, 2, 3);
        GameHelper.createBet("bet-2", user, game2, 1, 1);

        GameHelper.createScore("score-1", game1, 0, 3);
        GameHelper.createScore("score-2", game2, 2, 2);

        int correctGames = service.getCorrectGames(user);

        assertEquals(0, correctGames);
    }

    @Test
    @DisplayName("getCorrectGames(User) - should return zero if user has no active bets")
    void shouldReturnZeroIfUserHasNoActiveBets() {
        User user = new User();
        user.setBets(List.of());

        int correctGames = service.getCorrectGames(user);

        assertEquals(0, correctGames);
    }

    @Test
    @DisplayName("getCorrectGames(User) - should return zero if all bets are inactive")
    void shouldReturnZeroIfAllBetsAreInactiveInCorrectGames() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        Bet bet1 = GameHelper.createBet("bet-1", user, game1, 2, 3);
        bet1.setDeletedAt(LocalDateTime.now());
        Bet bet2 = GameHelper.createBet("bet-2", user, game2, 1, 1);
        bet2.setDeletedAt(LocalDateTime.now());

        int correctGames = service.getCorrectGames(user);

        assertEquals(0, correctGames);
    }

    @Test
    @DisplayName("getCorrectGames(User) - should handle null game results gracefully")
    void shouldHandleNullGameResultsGracefully() {
        User user = new User();
        GameHelper.createBet("bet-1", user, GameHelper.createGame("game-1"), 2, 3);
        GameHelper.createBet("bet-2", user, GameHelper.createGame("game-2"), 1, 1);

        int correctGames = service.getCorrectGames(user);

        assertEquals(0, correctGames);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should return zero if user has no bets")
    void shouldReturnZeroIfUserHasNoBetsInJokersWasted() {
        User user = new User();
        user.setBets(List.of());

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(0, jokersWasted);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should return zero if all bets are inactive")
    void shouldReturnZeroIfAllBetsAreInactiveInJokersWasted() {
        User user = new User();

        Bet bet1 = GameHelper.createBet("bet-1", user, GameHelper.createGame("game-1"), 2, 3, 2);
        bet1.setDeletedAt(LocalDateTime.now());
        Bet bet2 = GameHelper.createBet("bet-2", user, GameHelper.createGame("game-2"), 1, 1, 3);
        bet2.setDeletedAt(LocalDateTime.now());

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(0, jokersWasted);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should return zero if no jokers are used")
    void shouldReturnZeroIfNoJokersUsed() {
        User user = new User();
        GameHelper.createBet("bet-1", user, GameHelper.createGame("game-1"), 2, 3);
        GameHelper.createBet("bet-2", user, GameHelper.createGame("game-2"), 1, 1);

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(0, jokersWasted);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should return correct count for bets with wasted jokers")
    void shouldReturnCorrectCountForBetsWithWastedJokers() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");

        GameHelper.createBet("bet-1", user, game1, 2, 3, 2);
        GameHelper.createBet("bet-2", user, game2, 1, 1, 3);

        GameHelper.createScore("score-1", game1, 4, 0);
        GameHelper.createScore("score-2", game2, 0, 3);

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(3, jokersWasted);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should count triple joker as 2 wasted jokers")
    void shouldCountTripleJokerAs2WastedJokers() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        GameHelper.createBet("bet-1", user, game1, 2, 3, 3);
        GameHelper.createScore("score-1", game1, 4, 0);

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(2, jokersWasted);
    }

    @Test
    @DisplayName("getJokersWasted(User) - should count triple joker as 2 wasted jokers")
    void shouldCountDoubleJokerAs1WastedJokers() {
        User user = new User();

        Game game1 = GameHelper.createGame("game-1");
        GameHelper.createBet("bet-1", user, game1, 2, 3, 2);
        GameHelper.createScore("score-1", game1, 4, 0);

        int jokersWasted = service.getJokersWasted(user);

        assertEquals(1, jokersWasted);
    }
}
