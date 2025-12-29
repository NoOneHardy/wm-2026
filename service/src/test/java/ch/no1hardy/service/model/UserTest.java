package ch.no1hardy.service.model;

import ch.no1hardy.service.TestUtils;
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
    @DisplayName("isConfirmed() - should return false by default")
    void isConfirmed01() {
        assertFalse(user.isConfirmed());
    }

    @Test
    @DisplayName("isConfirmed() - should return false if application status is denied")
    void isConfirmed02() {
        user.deny();
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        assertFalse(user.isConfirmed());
    }

    @Test
    @DisplayName("isConfirmed() - should return false if email is not confirmed")
    void isConfirmed03() {
        user.approve();
        user.setEmailVerifiedAt(null);
        user.setDeletedAt(null);

        assertFalse(user.isConfirmed());
    }

    @Test
    @DisplayName("isConfirmed() - should return false if user is not active")
    void isConfirmed04() {
        user.approve();
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setDeletedAt(LocalDateTime.now());

        assertFalse(user.isConfirmed());
    }

    @Test
    @DisplayName("isConfirmed() - should return true if user matches all criteria for being confirmed")
    void isConfirmed05() {
        user.approve();
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        assertTrue(user.isConfirmed());
    }

    @Test
    @DisplayName("approve() - should approve user")
    void approve01() {
        assertFalse(user.isApproved());

        TestUtils.checkTime(user::approve, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
    }

    @Test
    @DisplayName("approve() - should approve user if not already confirmed")
    void approve02() {
        assertFalse(user.isApproved());

        TestUtils.CheckTimeResult firstApproval = TestUtils.checkTime(user::approve, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());

        TestUtils.sleep(10);

        TestUtils.checkTime(user::approve, user::getApplicationReviewedAt, false);
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
        TestUtils.checkTimeByResult(user.getApplicationReviewedAt(), firstApproval);
    }

    @Test
    @DisplayName("approve() - should approve user if denied")
    void approve03() {
        TestUtils.CheckTimeResult denialResult = TestUtils.checkTime(user::deny, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());

        TestUtils.sleep(10);

        TestUtils.checkTime(user::approve, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
        TestUtils.checkTimeByResult(user.getApplicationReviewedAt(), denialResult, false);
    }

    @Test
    @DisplayName("deny() - should deny user")
    void deny01() {
        assertFalse(user.isDenied());

        TestUtils.checkTime(user::deny, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
    }

    @Test
    @DisplayName("deny() - should deny user if approved")
    void deny02() {
        TestUtils.CheckTimeResult approvalResult = TestUtils.checkTime(user::approve, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());

        TestUtils.sleep(10);

        TestUtils.checkTime(user::deny, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        TestUtils.checkTimeByResult(user.getApplicationReviewedAt(), approvalResult, false);
    }

    @Test
    @DisplayName("deny() - should not deny user if already denied")
    void deny03() {
        TestUtils.CheckTimeResult firstDenial = TestUtils.checkTime(user::deny, user::getApplicationReviewedAt);
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());

        TestUtils.sleep(10);

        TestUtils.checkTime(user::deny, user::getApplicationReviewedAt, false);
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        TestUtils.checkTimeByResult(user.getApplicationReviewedAt(), firstDenial);
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
    void createVerificationCodeExpiration01() {
        User user = new User();
        int validMinutes = 15;

        TestUtils.checkTime(() -> {
            VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, validMinutes);
            assertNotNull(code);
            assertFalse(code.isExpired());
            return code;
        }, VerificationCode::getExpiresAt, true,15);
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
