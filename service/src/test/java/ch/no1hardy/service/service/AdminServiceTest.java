package ch.no1hardy.service.service;

import ch.no1hardy.service.TestUtils;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private AdminService service;

    @Test
    @DisplayName("approveUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInConfirm() {
        when(userService.getRaw("non-existing-user")).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> service.approveUser("non-existing-user"));
    }

    @Test
    @DisplayName("approveUser(String id) - should not confirm user if already confirmed")
    void approveUser01() {
        User user = new User();
        user.setId("user-1");
        TestUtils.CheckTimeResult firstApproval = TestUtils.checkTime(user::approve, user::getApplicationReviewedAt);
        when(userService.getRaw("user-1")).thenReturn(user);
        Mockito.doNothing().when(notificationService).notifyUserApproval(user);

        TestUtils.sleep(10);

        TestUtils.checkTime(() -> service.approveUser("user-1"), user::getApplicationReviewedAt, false);
        assertTrue(user.isApproved());
        TestUtils.checkTimeByResult(user.getApplicationReviewedAt(), firstApproval);
    }

    @Test
    @DisplayName("denyUser(String id) - should throw NotFoundException if no user is found")
    void shouldThrowNotFoundExceptionIfNoUserIsFoundInDeny() {
        when(userService.getRaw("non-existing-user")).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> service.denyUser("non-existing-user"));
    }

    @Test
    @DisplayName("denyUser(String id) - should not deny user if already denied")
    void shouldNotDenyUserIfAlreadyDenied() {
        User user = new User();
        user.setId("user-1");
        user.deny();
        when(userService.getRaw("user-1")).thenReturn(user);

        assertNotNull(user.getApplicationReviewedAt());
        user.setApplicationReviewedAt(LocalDateTime.of(2025, 6, 17, 14, 30));
        service.denyUser("user-1");
        assertFalse(user.isConfirmed());
        assertEquals(LocalDateTime.of(2025, 6, 17, 14, 30), user.getApplicationReviewedAt());
    }
}
