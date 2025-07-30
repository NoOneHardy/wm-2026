package ch.no1hardy.service.service;

import ch.no1hardy.service.config.GlobalKey;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.front.GlobalData;
import ch.no1hardy.service.mapper.NotificationMapperImpl;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GlobalDataService {
    private final UserService userService;
    private final NotificationMapperImpl notificationMapper;

    /**
     * Retrieves the current global data for the user.
     *
     * @return GlobalData containing notifications and other user-related information.
     * @throws UserNotFoundException if the current user is not found.
     */
    private GlobalData createGlobalData() throws UserNotFoundException {
        Optional<User> user$ = userService.getCurrentUserRaw();
        List<Notification> notifications = user$.isEmpty() ? List.of() : userService.getNotifications(user$.get());

        return GlobalData.builder()
                .id(UUID.randomUUID().toString())
                .notifications(notificationMapper.toDto(notifications))
                .build();
    }

    /**
     * Updates the global data in the request attributes.
     * This method should be called to refresh the global data for the current request.
     */
    public void updateGlobalData() {
        GlobalData globalData = createGlobalData();

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            attributes.setAttribute(GlobalKey.GLOBAL_DATA.getKey(), globalData, RequestAttributes.SCOPE_REQUEST);
        }
    }
}
