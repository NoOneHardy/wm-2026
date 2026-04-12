package ch.no1hardy.service.service;

import ch.no1hardy.service.mapper.PreferencesMapper;
import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import ch.no1hardy.service.model.preferences.NotificationPreferenceRepository;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.provider.PreferenceKeyProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PreferencesServiceTest {
    private PreferencesService service;

    @BeforeEach
    void beforeEach() {
        AuthService authService = Mockito.mock(AuthService.class);
        PreferencesMapper mapper = Mockito.mock(PreferencesMapper.class);
        NotificationPreferenceRepository notificationPrefRepo = Mockito.mock(NotificationPreferenceRepository.class);
        PreferenceKeyProvider preferenceKeyProvider = new PreferenceKeyProvider();

        service = new PreferencesService(authService, mapper, notificationPrefRepo, preferenceKeyProvider);
    }

    @Test
    @DisplayName("ensureNotificationPrefs(User) - should return existing and generated preferences")
    void ensureNotificationPrefs01() {
        User user = Mockito.mock(User.class);
        NotificationPreference pref1 = new NotificationPreference();
        pref1.setChannel(Channel.EMAIL);
        pref1.setType(NotificationType.APPROVAL);
        pref1.setUser(user);

        NotificationPreference pref2 = new NotificationPreference();
        pref2.setChannel(Channel.IN_APP);
        pref2.setType(NotificationType.RANKING_UPDATE);
        pref2.setUser(user);

        List<NotificationPreference> existingPreferences = List.of(pref1, pref2);
        when(user.getNotificationPreferences()).thenReturn(existingPreferences);

        List<NotificationPreference> result = service.ensureNotificationPrefs(user);

        assertEquals(3, result.size());
        assertNotEquals(existingPreferences.get(1), result.getFirst());

        boolean hasMultipleEmailApproval = result.stream()
                .filter(p -> p.getChannel() == Channel.IN_APP && p.getType() == NotificationType.RANKING_UPDATE)
                .count() > 1;
        assertFalse(hasMultipleEmailApproval);
    }

    @Test
    @DisplayName("ensureNotificationPrefs(User) - should generate defaults")
    void ensureNotificationPrefs02() {
        User user = Mockito.mock(User.class);
        user.setNotificationPreferences(List.of());

        List<NotificationPreference> result = service.ensureNotificationPrefs(user);
        assertEquals(3, result.size());

        checkPref(result.getFirst(), NotificationType.NEW_GAME);
        checkPref(result.get(1), NotificationType.NEW_RESULT);
        checkPref(result.get(2), NotificationType.RANKING_UPDATE);
    }

    void checkPref(NotificationPreference preference, NotificationType type) {
        assertEquals(Channel.IN_APP, preference.getChannel());
        assertEquals(type, preference.getType());
    }
}
