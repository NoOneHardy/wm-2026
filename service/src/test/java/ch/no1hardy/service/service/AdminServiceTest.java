package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {
    @MockitoBean
    private UserService userService;

    @Autowired
    private AdminService service;

    @Test
    @DisplayName("confirmUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInConfirm() {
        when(userService.getRaw("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.confirmUser("non-existing-user"));
    }

    @Test
    @DisplayName("confirmUser(String id) - should not confirm user if already confirmed")
    void shouldNotConfirmUserIfAlreadyConfirmed() {
        User user = new User();
        user.setId("user-1");
        user.confirm();
        when(userService.getRaw("user-1")).thenReturn(Optional.of(user));

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.confirmUser("user-1");
        assertTrue(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }

    @Test
    @DisplayName("denyUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInDeny() {
        when(userService.getRaw("non-existing-user")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> service.denyUser("non-existing-user"));
    }

    @Test
    @DisplayName("denyUser(String id) - should not deny user if already denied")
    void shouldNotDenyUserIfAlreadyDenied() {
        User user = new User();
        user.setId("user-1");
        user.deny();
        when(userService.getRaw("user-1")).thenReturn(Optional.of(user));

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.denyUser("user-1");
        assertFalse(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }
}
