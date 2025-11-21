package ch.no1hardy.service.model.preferences;

import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.user.User;

public record NotificationPreferenceKey(Channel channel, NotificationType type) {
    public NotificationPreference persist(User user) {
        NotificationPreference preference = new NotificationPreference();
        preference.setChannel(channel);
        preference.setType(type);
        preference.setUser(user);
        return preference;
    }
}
