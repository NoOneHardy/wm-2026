package ch.no1hardy.service.front.notification;

import ch.no1hardy.service.model.notification.NotificationType;

public record NotificationRes(
        String id,
        String title,
        String content,
        String route,
        NotificationType type
) {
}
