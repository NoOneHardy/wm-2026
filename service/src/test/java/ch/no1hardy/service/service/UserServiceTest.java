package ch.no1hardy.service.service;

import ch.no1hardy.service.SecurityHelper;
import ch.no1hardy.service.TestUtils;
import ch.no1hardy.service.exception.verification.VerificationException;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeRepository;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

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

    @MockitoSpyBean
    private UserRepository repository;

    @MockitoBean
    private GroupRepository groupRepository;

    @MockitoBean
    private AuthService authService;

    @MockitoSpyBean
    private VerificationCodeRepository verificationCodeRepository;

    @MockitoSpyBean
    private MailService mailService;

    @BeforeEach
    void beforeEach() {
        verificationCodeRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    @DisplayName("should create")
    void contextLoads() {
        assertNotNull(service);
    }

    @Test
    @DisplayName("listAll() - should return all users including deleted ones")
    void listAll01() {
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
    void list01() {
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
    @DisplayName("check(String, String) - should check if username and email are available")
    void check01() {
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
    void isUsernameAvailable01() {
        User user = new User();
        user.setUsername("No1Hardy");

        when(repository.findByUsername("No1Hardy")).thenReturn(Optional.of(user));

        assertFalse(service.isUsernameAvailable("No1Hardy"));
    }

    @Test
    @DisplayName("isUsernameAvailable() - should return true if username is available")
    void isUsernameAvailable02() {
        assertTrue(service.isUsernameAvailable("NoOneHardy"));
    }

    @Test
    @DisplayName("isUsernameAvailable() - should ignore inactive users")
    void isUsernameAvailable03() {
        User user = new User();
        user.setUsername("No1Hardy");
        user.setDeletedAt(LocalDateTime.now());

        when(repository.findByUsername("No1Hardy")).thenReturn(Optional.of(user));

        assertTrue(service.isUsernameAvailable("No1Hardy"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should return false if username is not available")
    void isEmailAvailable01() {
        User user = new User();
        user.setEmail("no1hardy@no1hardy.ch");

        when(repository.findByEmail("no1hardy@no1hardy.ch")).thenReturn(Optional.of(user));

        assertFalse(service.isEmailAvailable("no1hardy@no1hardy.ch"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should return true if username is available")
    void isEmailAvailable02() {
        assertTrue(service.isEmailAvailable("noonehardy@no1hardy.ch"));
    }

    @Test
    @DisplayName("isEmailAvailable() - should ignore inactive users")
    void isEmailAvailable03() {
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

        checkPoints(bet, score, 10);

        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 10);

        bet.setScoreTeamGuest(0);
        bet.setScoreTeamHome(0);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(0);
        checkPoints(bet, score, 10);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 7);


        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 7);
    }

    @Test
    void shouldCalculatePointsForTieWithIncorrectGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 5);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 6);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 6);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithoutAnyBonus() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(4);

        checkPoints(bet, score, 5);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(4);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 5);
    }

    @Test
    void shouldCalculatePointsForCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 2);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 2);
    }

    @Test
    void shouldCalculatePointsForCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 1);

        bet.setScoreTeamGuest(4);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(2);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 1);
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
    @DisplayName("UserRes create(UserReq) - should create new user")
    void create01() {
        UserReq dto = new UserReq();
        dto.setUsername("No1Hardy");
        dto.setEmail("no1hardy@test.ch");
        dto.setFirstname("Silas");
        dto.setLastname("Hardy");
        dto.setPassword("securepassword");

        UserRes response = service.create(dto);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("No1Hardy", response.getUsername());
        assertEquals("no1hardy@test.ch", response.getEmail());
        assertEquals("Silas", response.getFirstname());
        assertEquals("Hardy", response.getLastname());

        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("UserRes create(UserReq) - should hash password")
    void create02() {
        UserReq dto = new UserReq();
        dto.setUsername("No1Hardy");
        dto.setEmail("no1hardy@test.ch");
        dto.setFirstname("Silas");
        dto.setLastname("Hardy");
        dto.setPassword("securepassword");

        UserRes response = service.create(dto);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("No1Hardy", response.getUsername());
        assertEquals("no1hardy@test.ch", response.getEmail());
        assertEquals("Silas", response.getFirstname());
        assertEquals("Hardy", response.getLastname());

        repository.findById(response.getId())
                .ifPresentOrElse(
                        user -> assertNotEquals("securepassword", user.getPassword()),
                        () -> fail("UserRes create(UserReq) - should hash password")
                );
    }

    @Test
    @DisplayName("UserRes create(UserReq) - should send verification email")
    void create03() {
        UserReq dto = new UserReq();
        dto.setUsername("No1Hardy");
        dto.setEmail("silas@test.ch");
        dto.setFirstname("Silas");
        dto.setLastname("Hardy");
        dto.setPassword("securepassword");

        UserRes response = service.create(dto);
        assertNotNull(response);
        Mockito.verify(mailService, Mockito.times(1))
                .sendEmailVerificationMail(ArgumentMatchers.any(User.class), ArgumentMatchers.any(VerificationCode.class));
    }

    @Test
    @DisplayName("UserRes update(String, UserReq) - should update user information")
    void update01() {
        UserReq dto = new UserReq();
        dto.setUsername("No1HardyUpdated");
        dto.setEmail("silas@test-update.ch");
        dto.setFirstname("SilasUpdated");
        dto.setLastname("HardyUpdated");
        dto.setPassword("newsecurepassword");

        User existingUser = new User();
        existingUser.setId("user-id-123");
        existingUser.setUsername("No1Hardy");
        existingUser.setEmail("silas@test.ch");
        existingUser.setFirstname("Silas");
        existingUser.setLastname("Hardy");
        existingUser.setPassword("oldhashedpassword");

        Mockito.doReturn(existingUser).when(repository).save(ArgumentMatchers.any());
        Mockito.doReturn(Optional.of(existingUser)).when(repository).findById("user-id-123");
        service.update("user-id-123", dto);

        assertEquals("No1HardyUpdated", existingUser.getUsername());
        // TODO: Re-enable email update test after implementing email change verification
        // assertEquals("silas@test-update.ch", existingUser.getEmail());
        assertEquals("SilasUpdated", existingUser.getFirstname());
        assertEquals("HardyUpdated", existingUser.getLastname());
        assertNotEquals("oldhashedpassword", existingUser.getPassword());
    }

    @Test
    @DisplayName("UserRes update(String, UserReq) - should ignore ommitted properties")
    void update02() {
        UserReq dto = new UserReq();
        dto.setUsername("No1HardyUpdated");

        User existingUser = new User();
        existingUser.setId("user-id-123");
        existingUser.setUsername("No1Hardy");
        existingUser.setEmail("silas@test.ch");
        existingUser.setFirstname("Silas");
        existingUser.setLastname("Hardy");
        existingUser.setPassword("oldhashedpassword");

        Mockito.doReturn(existingUser).when(repository).save(ArgumentMatchers.any());
        Mockito.doReturn(Optional.of(existingUser)).when(repository).findById("user-id-123");

        service.update("user-id-123", dto);

        assertEquals("No1HardyUpdated", existingUser.getUsername());
        assertEquals("silas@test.ch", existingUser.getEmail());
        assertEquals("Silas", existingUser.getFirstname());
        assertEquals("Hardy", existingUser.getLastname());
        assertEquals("oldhashedpassword", existingUser.getPassword());
    }

    @Test
    @DisplayName("UserRes getCurrentUser() - should return current user")
    void getCurrentUser01() {
        User user = new User();
        user.setId("user-id-123");

        SecurityHelper.mockUserLogin(authService, user);

        UserRes currentUser = service.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("user-id-123", currentUser.getId());
        assertInstanceOf(UserRes.class, currentUser);
    }

    @Test
    @DisplayName("UserRes getCurrentUser() - should return empty UserRes when no user is logged in")
    void getCurrentUser02() {
        SecurityHelper.mockNoLogin(authService);
        UserRes currentUser = service.getCurrentUser();
        assertNotNull(currentUser);
        assertNull(currentUser.getId());
        assertInstanceOf(UserRes.class, currentUser);
    }

    @Test
    @DisplayName("verifyEmail(VerifyEmailReq) - should verify email and return updated user")
    void verifyEmail01() {
        User user = new User();
        user.setId("user-1");
        Mockito.doReturn(user).when(repository).save(ArgumentMatchers.any());
        Mockito.doReturn(Optional.of(user)).when(repository).findById("user-1");

        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
        when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

        VerifyEmailReq req = new VerifyEmailReq(code.getCode());

        TestUtils.checkTime(() -> {
            UserRes res = service.verifyEmail(req);
            assertEquals("user-1", res.getId());
        }, user::getEmailVerifiedAt);
    }

    @Test
    @DisplayName("verifyEmail(VerifyEmailReq) - should throw VerificationException when no code is found")
    void verifyEmail02() {
        try {
            User user = new User();
            user.setId("user-1");
            when(repository.findById("user-1")).thenReturn(Optional.of(user));

            service.verifyEmail(new VerifyEmailReq("invalid-code"));
        } catch (Exception e) {
            assertEquals(VerificationException.class, e.getClass());
            assertEquals("Invalid verification code", e.getMessage());
        }
    }

    @Test
    @DisplayName("verifyEmail(VerifyEmailReq) - should delete verification code after confirming email")
    void verifyEmail03() {
        User user = new User();
        user.setId("user-1");
        Mockito.doReturn(user).when(repository).save(ArgumentMatchers.any());
        Mockito.doReturn(Optional.of(user)).when(repository).findById("user-1");

        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
        when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

        VerifyEmailReq req = new VerifyEmailReq(code.getCode());
        service.verifyEmail(req);
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).delete(code);
    }

    @Test
    @DisplayName("verifyEmail(VerifyEmailReq) - should throw VerificationException if code is invalid")
    void verifyEmail04() {
        try {
            User user = new User();
            user.setId("user-1");
            when(repository.findById("user-1")).thenReturn(Optional.of(user));

            VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
            when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

            VerifyEmailReq req = new VerifyEmailReq("ABC1234");
            service.verifyEmail(req);
        } catch (Exception e) {
            assertEquals(VerificationException.class, e.getClass());
            assertEquals("Invalid verification code", e.getMessage());
        }
    }
}
