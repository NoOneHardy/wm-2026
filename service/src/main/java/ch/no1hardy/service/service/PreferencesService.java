package ch.no1hardy.service.service;

import ch.no1hardy.service.front.preferences.NotificationPreferenceRes;
import ch.no1hardy.service.mapper.PreferencesMapper;
import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import ch.no1hardy.service.model.preferences.NotificationPreferenceRepository;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PreferencesService {
    private final AuthService authService;
    private final PreferencesMapper mapper;
    private final NotificationPreferenceRepository notificationPreferenceRepository;

    public Optional<List<NotificationPreferenceRes>> getNotificationPreferences() {
        return getNotificationPreferencesRaw().map(mapper::toDto);
    }

    public Optional<List<NotificationPreference>> getNotificationPreferencesRaw() {
        return getNotificationPreferencesRaw(authService.getLoggedInUser());
    }

    public Optional<List<NotificationPreference>> getNotificationPreferencesRaw(Optional<User> user) {
        return user.map(this::ensureNotificationPreferences);
    }

    public List<NotificationPreference> ensureNotificationPreferences(@NotNull User user) {
        List<NotificationPreference> prefs = user.getNotificationPreferences();
        Set<NotificationPreferenceKey> existingKeys = getExistingPreferenceKeys(prefs); // Using set for lookup efficiency

        List<NotificationPreference> newPrefs = new ArrayList<>();

        for (Channel channel : Channel.values()) {
            for (NotificationType type : NotificationType.values()) {
                NotificationPreferenceKey key = new NotificationPreferenceKey(channel, type);
                if (existingKeys.contains(key)) continue;

                newPrefs.add(key.persist(user));
            }
        }

        if (!newPrefs.isEmpty()) {
            notificationPreferenceRepository.saveAll(newPrefs);
            prefs.addAll(newPrefs);
        }

        return prefs;
    }

    private Set<NotificationPreferenceKey> getExistingPreferenceKeys(List<NotificationPreference> preferences) {
        return preferences.stream()
                .map(p -> new NotificationPreferenceKey(p.getChannel(), p.getType()))
                .collect(Collectors.toSet());
    }

    private record NotificationPreferenceKey(Channel channel, NotificationType type) {
        NotificationPreference persist(User user) {
            NotificationPreference preference = new NotificationPreference();
            preference.setChannel(channel);
            preference.setType(type);
            preference.setUser(user);
            return preference;
        }
    }
}
