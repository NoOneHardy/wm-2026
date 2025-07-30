package ch.no1hardy.service.service;

import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Score;
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
}
