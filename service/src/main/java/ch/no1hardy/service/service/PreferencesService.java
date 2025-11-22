package ch.no1hardy.service.service;

import ch.no1hardy.service.front.preferences.NotificationPreferenceReq;
import ch.no1hardy.service.front.preferences.NotificationPreferenceRes;
import ch.no1hardy.service.mapper.PreferencesMapper;
import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import ch.no1hardy.service.model.preferences.NotificationPreferenceKey;
import ch.no1hardy.service.model.preferences.NotificationPreferenceRepository;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.provider.PreferenceKeyProvider;
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
    private final PreferenceKeyProvider preferenceKeyProvider;

    /**
     * Get notification preferences for the logged-in user as DTOs
     *
     * @return a list of notification preference DTOs wrapped in an Optional
     */
    public Optional<List<NotificationPreferenceRes>> getNotificationPrefs() {
        return getNotificationPrefsRaw().map(mapper::toDto);
    }

    /**
     * Get notification preferences for the logged-in user
     *
     * @return a list of notification preferences wrapped in an Optional
     */
    public Optional<List<NotificationPreference>> getNotificationPrefsRaw() {
        return authService.getLoggedInUser().map(this::getNotificationPrefsRaw);
    }

    /**
     * Get notification preferences for a specific user
     *
     * @param user the user whose preferences are to be retrieved
     * @return the user's notification preferences
     */
    public List<NotificationPreference> getNotificationPrefsRaw(@NotNull User user) {
        return ensureNotificationPrefs(user);
    }

    /**
     * Ensure that all notification preferences exist for the given user.
     * If any preferences are missing, they will be created with default values.
     *
     * @param user the user for whom to ensure notification preferences
     * @return the complete list of notification preferences for the user
     */
    public List<NotificationPreference> ensureNotificationPrefs(@NotNull User user) {
        List<NotificationPreferenceKey> keys = preferenceKeyProvider.getNotificationPreferenceKeys();

        List<NotificationPreference> existingPrefs = keys.stream().map(key -> notificationPreferenceRepository
                .findByUserAndChannelAndType(user, key.channel(), key.type())
        ).filter(Optional::isPresent).map(Optional::get).toList();
        List<NotificationPreference> prefs = new ArrayList<>(existingPrefs);

        if (prefs.size() == keys.size()) return prefs;


        Set<NotificationPreferenceKey> existingKeys = prefs.stream()
                .map(p -> new NotificationPreferenceKey(p.getChannel(), p.getType()))
                .collect(Collectors.toSet()); // Using set for lookup efficiency

        List<NotificationPreference> newPrefs = keys.stream()
                .filter(k -> !existingKeys.contains(k))
                .map(k -> k.persist(user))
                .toList();

        if (!newPrefs.isEmpty()) {
            notificationPreferenceRepository.saveAll(newPrefs);
            prefs.addAll(newPrefs);
        }

        return prefs;
    }

    /**
     * Check if a specific notification type is disabled for a user on a given channel
     *
     * @param user    the user to check
     * @param channel the notification channel
     * @param type    the notification type
     * @return true if the notification is disabled, false otherwise
     */
    public boolean hasDisabledNotification(@NotNull User user, @NotNull Channel channel, @NotNull NotificationType type) {
        return getNotificationPrefsRaw(user).stream()
                .filter(pref -> pref.getChannel().equals(channel) && pref.getType().equals(type))
                .findFirst()
                .map(p -> !p.isSelected())
                .orElse(false); // fallback to false if no preference is found
    }

    /**
     * Update notification preferences for the logged-in user
     *
     * @param prefs the list of notification preference requests to update
     * @return the updated list of notification preference DTOs wrapped in an Optional
     */
    public Optional<List<NotificationPreferenceRes>> updateNotificationPrefs(List<NotificationPreferenceReq> prefs) {
        getNotificationPrefsRaw().ifPresent(existingPrefs ->
                prefs.forEach(req -> existingPrefs.stream()    // loop over incoming prefs
                        .filter(ep -> ep.compareToReq(req)).findFirst() // find matching existing pref
                        .ifPresent(ep -> {                        // update and save
                            ep.setSelected(req.selected());
                            notificationPreferenceRepository.save(ep);
                        })));
        return getNotificationPrefs();
    }
}
