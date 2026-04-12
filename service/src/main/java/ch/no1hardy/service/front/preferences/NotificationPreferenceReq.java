package ch.no1hardy.service.front.preferences;

import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;

public record NotificationPreferenceReq(
        NotificationType type,
        Channel channel,
        boolean selected
) {
}
