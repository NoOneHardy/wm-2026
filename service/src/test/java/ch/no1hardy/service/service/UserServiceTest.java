package ch.no1hardy.service.service;

import ch.no1hardy.service.SecurityHelper;
import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.leaderboard.RankingRes;
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
    private UserRepository userRepository;

    @MockitoBean
    private GroupRepository groupRepository;

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

        when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3, user4));

        List<RankingRes> res = service.getLeaderboard();

        assertEquals(user1.getId(), res.get(3).getId());
        assertEquals(user2.getId(), res.get(0).getId());
        assertEquals(user3.getId(), res.get(1).getId());
        assertEquals(user4.getId(), res.get(2).getId());
    }

    @Test
    @DisplayName("confirmUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInConfirm() {
        when(userRepository.findById("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.confirmUser("non-existing-user"));
    }

    @Test
    @DisplayName("confirmUser(String id) - should confirm user")
    void shouldConfirmUser() {
        User user = new User();
        user.setId("user-1");
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        service.confirmUser("user-1");
        assertTrue(user.isConfirmed());
    }

    @Test
    @DisplayName("confirmUser(String id) - should not confirm user if already confirmed")
    void shouldNotConfirmUserIfAlreadyConfirmed() {
        User user = new User();
        user.setId("user-1");
        user.confirm();
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.confirmUser("user-1");
        assertTrue(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }

    @Test
    @DisplayName("denyUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInDeny() {
        when(userRepository.findById("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.denyUser("non-existing-user"));
    }

    @Test
    @DisplayName("denyUser(String id) - should deny user")
    void shouldDenyUser() {
        User user = new User();
        user.setId("user-1");
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

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
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

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
        SecurityHelper.mockUserLogin(null);
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
        when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        SecurityHelper.mockUserLogin(user1);
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user1));

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

        when(userRepository.findAll()).thenReturn(List.of());

        SecurityHelper.mockUserLogin(user1);
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user1));

        UserSummary summary = service.getUserSummary();
        assertNull(summary.getIsConfirmed());

        user1.confirm();
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user1));
        summary = service.getUserSummary();
        assertTrue(summary.getIsConfirmed());

        user1.deny();
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user1));
        summary = service.getUserSummary();
        assertFalse(summary.getIsConfirmed());
    }

    @Test
    @DisplayName("getUserSummary() - should set ranking to null if user leaderboard is empty")
    void shouldSetRankingToNullIfUserLeaderboardIsEmpty() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(1000);
        when(userRepository.findAll()).thenReturn(List.of(user1));

        SecurityHelper.mockUserLogin(user1);
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user1));

        UserSummary summary = service.getUserSummary();
        assertNull(summary.getRanking());
    }
}
