package ch.no1hardy.service.service;

import ch.no1hardy.service.GameHelper;
import ch.no1hardy.service.SecurityHelper;
import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService service;

    @MockitoBean
    private UserRepository repository;

    @MockitoBean
    private GroupRepository groupRepository;

    @Test
    @DisplayName("should create")
    void contextLoads() {
        assertNotNull(service);
    }

    @Test
    @DisplayName("listAll() - should return all users including deleted ones")
    void listAll() {
        User user1 = new User();
        user1.setUsername("No1Hardy");
        User user2 = new User();
        user2.setUsername("NoOneHardy");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(user1, user2));
        List<UserRes> users = service.listAll();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("No1Hardy", users.getFirst().getUsername());
        assertEquals("NoOneHardy", users.get(1).getUsername());
        assertInstanceOf(UserRes.class, users.getFirst());
        assertInstanceOf(UserRes.class, users.get(1));
    }

    @Test
    @DisplayName("list() - should return only active users")
    void list() {
        User user1 = new User();
        user1.setUsername("No1Hardy");
        User user2 = new User();
        user2.setUsername("NoOneHardy");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        List<UserRes> users = service.list();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("No1Hardy", users.getFirst().getUsername());
        assertInstanceOf(UserRes.class, users.getFirst());
    }

    @Test
    @DisplayName("should check if username and email are available")
    void shouldCheckIfUsernameAndEmailAreAvailable() {
        User user1 = new User();
        user1.setUsername("No1Hardy");
        user1.setEmail("no1hardy@no1hardy.ch");

        when(repository.findByUsername("No1Hardy")).thenReturn(Optional.of(user1));
        when(repository.findByEmail("no1hardy@no1hardy.ch")).thenReturn(Optional.of(user1));

        // Username without email
        CheckRes usernameWithoutEmail = service.check("No1Hardy", null);
        assertNotNull(usernameWithoutEmail);
        assertFalse(usernameWithoutEmail.getIsUsernameAvailable());
        assertTrue(usernameWithoutEmail.getIsEmailAvailable());

        // Email without username
        CheckRes emailWithoutUsername = service.check(null, "no1hardy@no1hardy.ch");
        assertNotNull(emailWithoutUsername);
        assertTrue(emailWithoutUsername.getIsUsernameAvailable());
        assertFalse(emailWithoutUsername.getIsEmailAvailable());

        // Email with username
        CheckRes emailWithUsername = service.check("No1Hardy", "no1hardy@no1hardy.ch");
        assertNotNull(emailWithUsername);
        assertFalse(emailWithUsername.getIsUsernameAvailable());
        assertFalse(emailWithUsername.getIsEmailAvailable());

        CheckRes emailWithUsernameAvailable = service.check("NoOneHardy", "noonehardy@no1hardy.ch");
        assertNotNull(emailWithUsernameAvailable);
        assertTrue(emailWithUsernameAvailable.getIsUsernameAvailable());
        assertTrue(emailWithUsernameAvailable.getIsEmailAvailable());
    }

    @Test
    @DisplayName("isUsernameAvailable() - should return false if username is not available")
    void shouldCheckIfUsernameIsNotAvailable() {
        User user = new User();
        user.setUsername("No1Hardy");

        when(repository.findByUsername("No1Hardy")).thenReturn(Optional.of(user));

        assertFalse(service.isUsernameAvailable("No1Hardy"));
    }

    @Test
    @DisplayName("isUsernameAvailable() - should return true if username is available")
    void shouldCheckIfUsernameIsAvailable() {
        assertTrue(service.isUsernameAvailable("NoOneHardy"));
    }

    @Test
    @DisplayName("isUsernameAvailable() - should ignore inactive users")
    void shouldIgnoreInactiveUsernames() {
        User user = new User();
        user.setUsername("No1Hardy");
        user.setDeletedAt(LocalDateTime.now());

        when(repository.findByUsername("No1Hardy")).thenReturn(Optional.of(user));

        assertTrue(service.isUsernameAvailable("No1Hardy"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should return false if username is not available")
    void shouldCheckIfEmailIsNotAvailable() {
        User user = new User();
        user.setEmail("no1hardy@no1hardy.ch");

        when(repository.findByEmail("no1hardy@no1hardy.ch")).thenReturn(Optional.of(user));

        assertFalse(service.isEmailAvailable("no1hardy@no1hardy.ch"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should return true if username is available")
    void shouldCheckIfEmailIsAvailable() {
        assertTrue(service.isEmailAvailable("noonehardy@no1hardy.ch"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should ignore inactive users")
    void shouldIgnoreInactiveEmails() {
        User user = new User();
        user.setEmail("no1hardy@no1hardy.ch");
        user.setDeletedAt(LocalDateTime.now());

        when(repository.findByEmail("no1hardy@no1hardy.ch")).thenReturn(Optional.of(user));

        assertTrue(service.isEmailAvailable("no1hardy@no1hardy.ch"));
    }

    @Test
    void shouldCalculatePointsForCorrectBet() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);

        Score score = new Score();
        score.setScoreTeamGuest(2);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 100);

        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 100);

        bet.setScoreTeamGuest(0);
        bet.setScoreTeamHome(0);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(0);
        checkPoints(bet, score, 100);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 70);


        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 70);
    }

    @Test
    void shouldCalculatePointsForTieWithIncorrectGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 50);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 60);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 60);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithoutAnyBonus() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(4);

        checkPoints(bet, score, 50);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(4);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 50);
    }

    @Test
    void shouldCalculatePointsForCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 20);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 20);
    }

    @Test
    void shouldCalculatePointsForCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 10);

        bet.setScoreTeamGuest(4);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(2);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 10);
    }

    @Test
    void shouldCalculatePointsForNoBonus() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 0);
    }

    Method getCalculateUserPointsMethod() throws NoSuchMethodException {
        Method method = UserService.class.getDeclaredMethod("calculateUserPoints", Bet.class, Score.class);
        method.setAccessible(true);
        return method;
    }

    void checkPoints(Bet bet, Score score, int expectedPoints) {
        try {
            bet.setJoker(1);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        try {
            bet.setJoker(2);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints * 2, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        try {
            bet.setJoker(3);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints * 3, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void shouldCalculateLeaderboard() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(100);
        user1.setLastReviewedPoints(50);
        user1.confirm();

        User user2 = new User();
        user2.setId("user-2");
        user2.setPoints(200);
        user2.setLastReviewedPoints(150);
        user2.confirm();

        User user3 = new User();
        user3.setId("user-3");
        user3.setPoints(200);
        user3.setLastReviewedPoints(100);
        user3.confirm();

        User user4 = new User();
        user4.setId("user-4");
        user4.setPoints(150);
        user4.setLastReviewedPoints(0);
        user4.confirm();

        when(repository.findAll()).thenReturn(List.of(user1, user2, user3, user4));

        List<RankingRes> res = service.getLeaderboard();

        assertEquals(user1.getId(), res.get(3).getId());
        assertEquals(user2.getId(), res.get(0).getId());
        assertEquals(user3.getId(), res.get(1).getId());
        assertEquals(user4.getId(), res.get(2).getId());
    }

    @Test
    @DisplayName("confirmUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInConfirm() {
        when(repository.findById("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.confirmUser("non-existing-user"));
    }

    @Test
    @DisplayName("confirmUser(String id) - should confirm user")
    void shouldConfirmUser() {
        User user = new User();
        user.setId("user-1");
        when(repository.findById("user-1")).thenReturn(Optional.of(user));

        service.confirmUser("user-1");
        assertTrue(user.isConfirmed());
    }

    @Test
    @DisplayName("confirmUser(String id) - should not confirm user if already confirmed")
    void shouldNotConfirmUserIfAlreadyConfirmed() {
        User user = new User();
        user.setId("user-1");
        user.confirm();
        when(repository.findById("user-1")).thenReturn(Optional.of(user));

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.confirmUser("user-1");
        assertTrue(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }

    @Test
    @DisplayName("denyUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInDeny() {
        when(repository.findById("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.denyUser("non-existing-user"));
    }

    @Test
    @DisplayName("denyUser(String id) - should deny user")
    void shouldDenyUser() {
        User user = new User();
        user.setId("user-1");
        when(repository.findById("user-1")).thenReturn(Optional.of(user));

        user.confirm();
        assertTrue(user.isConfirmed());
        service.denyUser("user-1");
        assertFalse(user.isConfirmed());
    }

    @Test
    @DisplayName("denyUser(String id) - should not deny user if already denied")
    void shouldNotDenyUserIfAlreadyDenied() {
        User user = new User();
        user.setId("user-1");
        user.deny();
        when(repository.findById("user-1")).thenReturn(Optional.of(user));

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.denyUser("user-1");
        assertFalse(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }

    @Test
    @DisplayName("getOverallPercentage() - should return 0.0 if there are no games")
    void shouldReturnZeroIfNoGames() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Group group = new Group();
        group.setGames(List.of());
        when(groupRepository.findAll()).thenReturn(List.of(group));

        assertEquals(0.0, service.getOverallPercentage(user));

        when(groupRepository.findAll()).thenReturn(List.of());
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
        when(groupRepository.findAll()).thenReturn(List.of(group));

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
        when(groupRepository.findAll()).thenReturn(List.of(group));

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
    @DisplayName("getUserSummary() - should return user summary with correct points and ranking")
    void shouldReturnUserSummaryWithCorrectPointsAndRanking() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(1000);
        user1.confirm();

        User user2 = new User();
        user2.setId("user-2");
        user2.setPoints(950);
        user2.confirm();

        User user3 = new User();
        user3.setId("user-3");
        user3.setPoints(1050);
        user3.confirm();
        when(repository.findAll()).thenReturn(List.of(user1, user2, user3));

        SecurityHelper.mockUserLogin(user1, repository);

        UserSummary summary = service.getUserSummary();

        assertEquals(1000, summary.getPoints());
        assertEquals(2, summary.getRanking());
        assertTrue(summary.getIsConfirmed());
        assertEquals(0.0, summary.getPercentage());
    }

    @Test
    @DisplayName("getUserSummary() - should evaluate whether user is confirmed")
    void shouldEvaluateWhetherUserIsConfirmed() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(1000);

        when(repository.findAll()).thenReturn(List.of());

        SecurityHelper.mockUserLogin(user1, repository);

        UserSummary summary = service.getUserSummary();
        assertNull(summary.getIsConfirmed());

        user1.confirm();
        when(repository.findById("user-1")).thenReturn(Optional.of(user1));
        summary = service.getUserSummary();
        assertTrue(summary.getIsConfirmed());

        user1.deny();
        when(repository.findById("user-1")).thenReturn(Optional.of(user1));
        summary = service.getUserSummary();
        assertFalse(summary.getIsConfirmed());
    }

    @Test
    @DisplayName("getUserSummary() - should set ranking to null if user leaderboard is empty")
    void shouldSetRankingToNullIfUserLeaderboardIsEmpty() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(1000);
        when(repository.findAll()).thenReturn(List.of(user1));

        SecurityHelper.mockUserLogin(user1, repository);

        UserSummary summary = service.getUserSummary();
        assertNull(summary.getRanking());
    }

    @Test
    @DisplayName("getStatistics() - should throw BadRequestException if no user is logged in")
    void shouldThrowBadRequestExceptionIfNoUserLoggedInForStatistics() {
        SecurityHelper.mockNoLogin();
        assertThrows(BadRequestException.class, service::getStatistics);
    }

    @Test
    @DisplayName("getStatistics() - should return correct statistics for logged-in user")
    void shouldReturnCorrectStatisticsForLoggedInUser() {
        User user = new User();
        user.setId("user-1");

        Game game1 = GameHelper.createGame("game-1");
        Game game2 = GameHelper.createGame("game-2");
        GameHelper.createBet("bet-1", user, game1, 2, 3);
        GameHelper.createBet("bet-2", user, game2, 1, 1, 2);

        GameHelper.createScore("score-1", game1, 2, 3);
        GameHelper.createScore("score-2", game2, 0, 4);

        SecurityHelper.mockUserLogin(user, repository);
        when(repository.findById("user-1")).thenReturn(Optional.of(user));

        Statistics stats = service.getStatistics();

        assertEquals(7, stats.getTotalGoalsBet());
        assertEquals(1, stats.getCorrectGames());
        assertEquals(1, stats.getJokersWasted());
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
