package ch.no1hardy.service.model;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
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
}
