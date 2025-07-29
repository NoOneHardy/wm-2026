package ch.no1hardy.service.front.notification;

import ch.no1hardy.service.model.notification.NotificationType;
import lombok.Data;

@Data
public class NotificationRes {
    private String id;
    private String title;
    private String content;
    private String route;
    private NotificationType type;
}
