package ch.no1hardy.service.model;

import ch.no1hardy.service.common.DateHelper;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId("user-0");
    }

    @Test
    void shouldSetUserUnconfirmedByDefault() {
        assertFalse(user.isConfirmed());
    }

    @Test
    void shouldNotConfirmUserIfApplicationStatusIsDenied() {
        user.deny();
        assertFalse(user.isConfirmed());
    }

    @Test
    void shouldConfirmUserIfApplicationStatusIsAccepted() {
        user.confirm();
        assertTrue(user.isConfirmed());
    }

    @Test
    void shouldNotConfirmUserIfUserIsNotActive() {
        user.confirm();
        user.setDeletedAt(LocalDateTime.now());

        assertFalse(user.isConfirmed());
    }

    @Test
    void shouldConfirmUserIfNotAlreadyConfirmed() {
        assertFalse(user.isConfirmed());
        user.confirm();
        assertTrue(user.isConfirmed());
        user.setApplicationReviewedAt(LocalDateTime.of(2026, 6, 17, 9, 0, 0));

        user.confirm();
        assertEquals(LocalDateTime.of(2026, 6, 17, 9, 0, 0), user.getApplicationReviewedAt());
    }

    @Test
    void shouldDenyUserIfNotAlreadyDenied() {
        user.deny();
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2026, 6, 17, 9, 0, 0));

        user.deny();
        assertEquals(LocalDateTime.of(2026, 6, 17, 9, 0, 0), user.getApplicationReviewedAt());
    }

    @Test
    void shouldAllowConfirmAfterDeny() {
        user.deny();
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2026, 6, 17, 9, 0, 0));

        user.confirm();
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
        assertNotEquals(LocalDateTime.of(2026, 6, 17, 9, 0, 0), user.getApplicationReviewedAt());
    }

    @Test
    void shouldAllowDenyAfterConfirm() {
        user.confirm();
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2026, 6, 17, 9, 0, 0));

        user.deny();
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        assertNotEquals(LocalDateTime.of(2026, 6, 17, 9, 0, 0), user.getApplicationReviewedAt());
    }

    @Test
    @DisplayName("Boolean isApproved() - should return false when reviewedAt is null even if status is ACCEPTED")
    void isApproved01() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setApplicationReviewedAt(null);

        assertFalse(user.isApproved());
    }

    @Test
    @DisplayName("Boolean isApproved() - should return false when status is null even if reviewedAt is set")
    void isApproved02() {
        User user = new User();
        user.setUserApplicationStatus(null);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertFalse(user.isApproved());
    }

    @Test
    @DisplayName("Boolean isApproved() - should return false when both status and reviewedAt are null")
    void isApproved03() {
        User user = new User();
        user.setUserApplicationStatus(null);
        user.setApplicationReviewedAt(null);

        assertFalse(user.isApproved());
    }

    @Test
    @DisplayName("Boolean isApproved() - should return false when status is DENIED and reviewedAt is set")
    void isApproved04() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertFalse(user.isApproved());
    }

    @Test
    @DisplayName("Boolean isApproved() - should return true when status is ACCEPTED and reviewedAt is set")
    void isApproved05() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertTrue(user.isApproved());
    }

    @Test
    @DisplayName("Boolean isDenied() - should return false when reviewedAt is null even if status is DENIED")
    void isDenied01() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        user.setApplicationReviewedAt(null);

        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("Boolean isDenied() - should return false when status is null even if reviewedAt is set")
    void isDenied02() {
        User user = new User();
        user.setUserApplicationStatus(null);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("Boolean isDenied() - should return false when both status and reviewedAt are null")
    void isDenied03() {
        User user = new User();
        user.setUserApplicationStatus(null);
        user.setApplicationReviewedAt(null);

        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("Boolean isDenied() - should return false when status is ACCEPTED and reviewedAt is set")
    void isDenied04() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("Boolean isDenied() - should return true when status is DENIED and reviewedAt is set")
    void isDenied05() {
        User user = new User();
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        user.setApplicationReviewedAt(LocalDateTime.now());

        assertTrue(user.isDenied());
    }

    @Test
    @DisplayName("VerificationCode createVerificationCode(VerificationCodeType type, int validMinutes) - should create a new verification code and add it to the user's list")
    void createVerificationCode() {
        User user = new User();
        int initialSize = user.getVerificationCodes().size();

        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);

        assertNotNull(code);
        assertEquals(VerificationCodeType.EMAIL, code.getType());
        assertEquals(user, code.getUser());
        assertEquals(initialSize + 1, user.getVerificationCodes().size());
        assertTrue(user.getVerificationCodes().contains(code));
    }

    @Test
    @DisplayName("VerificationCode createVerificationCode(VerificationCodeType type, int validMinutes) - should set the expiration time correctly")
    void createVerificationCodeExpiration() {
        User user = new User();
        int validMinutes = 15;

        LocalDateTime beforeCreation = LocalDateTime.now();
        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, validMinutes);
        LocalDateTime afterCreation = LocalDateTime.now();

        LocalDateTime expectedExpirationStart = beforeCreation.plusMinutes(validMinutes);
        LocalDateTime expectedExpirationEnd = afterCreation.plusMinutes(validMinutes);

        assertNotNull(code);
        assertFalse(code.isExpired());
        assertTrue(DateHelper.isBetween(code.getExpiresAt(), expectedExpirationStart, expectedExpirationEnd));
    }

    @Test
    @DisplayName("Optional<VerificationCode> getMostRecentEmailVerificationCode() - should return empty when no verification codes are present")
    void getMostRecentEmailVerificationCode01() {
        User user = new User();
        assertTrue(user.getMostRecentEmailVerificationCode().isEmpty());
    }

    @Test
    @DisplayName("Optional<VerificationCode> getMostRecentEmailVerificationCode() - should return the most recent email verification code when multiple are present")
    void getMostRecentEmailVerificationCode02() {
        User user = new User();

        VerificationCode code1 = user.createVerificationCode(VerificationCodeType.EMAIL, 5);
        VerificationCode code2 = user.createVerificationCode(VerificationCodeType.EMAIL, 10);

        code1.setCreatedAt(LocalDateTime.of(2025, 12, 17, 10, 0, 0));
        code2.setCreatedAt(LocalDateTime.of(2025, 12, 17, 9, 59, 59));

        Optional<VerificationCode> mostRecentCode = user.getMostRecentEmailVerificationCode();
        assertTrue(mostRecentCode.isPresent());
        assertEquals(code1.getId(), mostRecentCode.get().getId());
    }

    @Test
    @DisplayName("Optional<VerificationCode> getMostRecentEmailVerificationCode() - should return empty when all email verification codes are expired")
    void getMostRecentEmailVerificationCode03() {
        User user = new User();

        user.createVerificationCode(VerificationCodeType.EMAIL, -5);
        user.createVerificationCode(VerificationCodeType.EMAIL, -10);

        Optional<VerificationCode> mostRecentCode = user.getMostRecentEmailVerificationCode();
        assertTrue(mostRecentCode.isEmpty());
    }
}
