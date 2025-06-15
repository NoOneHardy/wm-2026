package ch.no1hardy.service.model;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        user.setApplicationReviewedAt(LocalDateTime.now());
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);

        assertFalse(user.isConfirmed());
    }

    @Test
    void shouldConfirmUserIfApplicationStatusIsAccepted() {
        user.setApplicationReviewedAt(LocalDateTime.now());
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);

        assertTrue(user.isConfirmed());
    }

    @Test
    void shouldNotConfirmUserIfUserIsNotActive() {
        user.setApplicationReviewedAt(LocalDateTime.now());
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setDeletedAt(LocalDateTime.now());

        assertFalse(user.isConfirmed());
    }
}
