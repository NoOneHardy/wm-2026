package ch.no1hardy.service.service;

import ch.no1hardy.service.config.GlobalKey;
import ch.no1hardy.service.front.GlobalData;
import ch.no1hardy.service.mapper.NotificationMapperImpl;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GlobalDataService {
    private final UserService userService;
    private final NotificationMapperImpl notificationMapper;

    private GlobalData createGlobalData() {
        User user = userService.getLoggedInUser();
        List<Notification> notifications;
        if (user != null) {
            notifications = userService.getNotifications(user);
        } else {
            notifications = List.of();
        }

        return GlobalData.builder()
                .id(UUID.randomUUID().toString())
                .notifications(notificationMapper.toDto(notifications))
                .build();
    }
    public void updateGlobalData() {
        GlobalData globalData = createGlobalData();

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            attributes.setAttribute(GlobalKey.GLOBAL_DATA.getKey(), globalData, RequestAttributes.SCOPE_REQUEST);
        }
    }
}
