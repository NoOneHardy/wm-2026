package ch.no1hardy.service.provider;

import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.preferences.NotificationPreferenceKey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceKeyProvider {
    public List<NotificationPreferenceKey> getNotificationPreferenceKeys() {
        return List.of(
                new NotificationPreferenceKey(Channel.IN_APP, NotificationType.NEW_GAME),
                new NotificationPreferenceKey(Channel.IN_APP, NotificationType.NEW_RESULT),
                new NotificationPreferenceKey(Channel.IN_APP, NotificationType.RANKING_UPDATE)
        );
    }
}
