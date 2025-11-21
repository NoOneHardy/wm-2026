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

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PreferencesService {
    private final AuthService authService;
    private final PreferencesMapper mapper;
    private final NotificationPreferenceRepository notificationPreferenceRepository;

    public Optional<List<NotificationPreferenceRes>> getNotificationPrefs() {
        return getNotificationPrefsRaw().map(mapper::toDto);
    }

    public Optional<List<NotificationPreference>> getNotificationPrefsRaw() {
        return getNotificationPrefsRaw(authService.getLoggedInUser());
    }

    public Optional<List<NotificationPreference>> getNotificationPrefsRaw(Optional<User> user) {
        return user.map(this::ensureNotificationPrefs);
    }

    public List<NotificationPreference> ensureNotificationPrefs(@NotNull User user) {
        List<NotificationPreference> prefs = new ArrayList<>(user.getNotificationPreferences());
        Set<NotificationPreferenceKey> existingKeys = getExistingPreferenceKeys(prefs); // Using set for lookup efficiency

        List<NotificationPreference> newPrefs = getAllNotificationPrefKeys().stream()
                .filter(k -> !existingKeys.contains(k))
                .map(k -> k.persist(user))
                .toList();

        if (!newPrefs.isEmpty()) {
            notificationPreferenceRepository.saveAll(newPrefs);
            prefs.addAll(newPrefs);
        }

        return prefs;
    }

    private List<NotificationPreferenceKey> getAllNotificationPrefKeys() {
        return Arrays.stream(Channel.values())
                .flatMap(channel -> Arrays.stream(NotificationType.values())
                        .map(type -> new NotificationPreferenceKey(channel, type)))
                .toList();
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
