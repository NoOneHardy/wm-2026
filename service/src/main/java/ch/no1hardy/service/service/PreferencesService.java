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

    public Optional<List<NotificationPreferenceRes>> getNotificationPrefs() {
        return getNotificationPrefsRaw().map(mapper::toDto);
    }

    public Optional<List<NotificationPreference>> getNotificationPrefsRaw() {
        return authService.getLoggedInUser().map(this::getNotificationPrefsRaw);
    }

    public List<NotificationPreference> getNotificationPrefsRaw(@NotNull User user) {
        return ensureNotificationPrefs(user);
    }

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

    public boolean hasDisabledNotification(@NotNull User user, @NotNull Channel channel, @NotNull NotificationType type) {
        return getNotificationPrefsRaw(user).stream()
                .filter(pref -> pref.getChannel().equals(channel) && pref.getType().equals(type))
                .findFirst()
                .map(p -> !p.isSelected())
                .orElse(false); // fallback to false if no preference is found
    }

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
