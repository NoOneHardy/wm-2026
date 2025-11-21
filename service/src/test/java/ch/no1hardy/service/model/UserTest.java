package ch.no1hardy.service.model;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
}
