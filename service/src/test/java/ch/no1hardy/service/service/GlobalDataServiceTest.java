package ch.no1hardy.service.service;

import ch.no1hardy.service.mapper.NotificationMapperImpl;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class GlobalDataServiceTest {
    @Autowired
    private GlobalDataService service;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private NotificationMapperImpl notificationMapper;

    @Test
    @DisplayName("updateGlobalData() - should pass empty list to mapper when no user is logged in")
    void shouldPassEmptyListToMapperWhenNotLoggedIn() {
        when(authService.getLoggedInUser()).thenReturn(Optional.empty());

        service.updateGlobalData();

        verify(notificationMapper).toDto(List.of());
        verify(userService, never()).getNotifications(any());
    }

    @Test
    @DisplayName("updateGlobalData() - should pass empty list to mapper when user has no notifications")
    void shouldPassEmptyListToMapperWhenUserHasNoNotifications() {
        User user = new User();
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(userService.getNotifications(user)).thenReturn(List.of());

        service.updateGlobalData();

        verify(notificationMapper).toDto(List.of());
    }

    @Test
    @DisplayName("updateGlobalData() - should sort notifications by createdAt descending before mapping")
    void shouldSortNotificationsByCreatedAtDescending() {
        User user = new User();
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        LocalDateTime base = LocalDateTime.of(2026, 6, 15, 12, 0);
        Notification oldest = notificationWithCreatedAt(base);
        Notification middle = notificationWithCreatedAt(base.plusHours(1));
        Notification newest = notificationWithCreatedAt(base.plusHours(2));

        when(userService.getNotifications(user)).thenReturn(List.of(oldest, newest, middle));

        ArgumentCaptor<List<Notification>> captor = ArgumentCaptor.captor();
        service.updateGlobalData();
        verify(notificationMapper).toDto(captor.capture());

        List<Notification> captured = captor.getValue();
        assertEquals(3, captured.size());
        assertEquals(newest, captured.get(0));
        assertEquals(middle, captured.get(1));
        assertEquals(oldest, captured.get(2));
    }

    @Test
    @DisplayName("updateGlobalData() - should limit notifications to 5 newest after sorting")
    void shouldLimitNotificationsToFiveNewest() {
        User user = new User();
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        LocalDateTime base = LocalDateTime.of(2026, 6, 15, 12, 0);
        List<Notification> notifications = List.of(
                notificationWithCreatedAt(base.plusHours(6)),
                notificationWithCreatedAt(base.plusHours(2)),
                notificationWithCreatedAt(base.plusHours(4)),
                notificationWithCreatedAt(base),
                notificationWithCreatedAt(base.plusHours(1)),
                notificationWithCreatedAt(base.plusHours(5)),
                notificationWithCreatedAt(base.plusHours(3))
        );
        when(userService.getNotifications(user)).thenReturn(notifications);

        ArgumentCaptor<List<Notification>> captor = ArgumentCaptor.captor();
        service.updateGlobalData();
        verify(notificationMapper).toDto(captor.capture());

        List<Notification> captured = captor.getValue();
        assertEquals(5, captured.size());
        assertEquals(base.plusHours(6), captured.get(0).getCreatedAt());
        assertEquals(base.plusHours(5), captured.get(1).getCreatedAt());
        assertEquals(base.plusHours(4), captured.get(2).getCreatedAt());
        assertEquals(base.plusHours(3), captured.get(3).getCreatedAt());
        assertEquals(base.plusHours(2), captured.get(4).getCreatedAt());
    }

    private Notification notificationWithCreatedAt(LocalDateTime createdAt) {
        Notification notification = new Notification();
        notification.setCreatedAt(createdAt);
        return notification;
    }
}
