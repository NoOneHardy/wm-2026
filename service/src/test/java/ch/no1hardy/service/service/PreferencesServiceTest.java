package ch.no1hardy.service.service;

import ch.no1hardy.service.mapper.PreferencesMapper;
import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import ch.no1hardy.service.model.preferences.NotificationPreferenceRepository;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

public class PreferencesServiceTest {
    private PreferencesService service;

    @BeforeEach
    void beforeEach() {
        AuthService authService = Mockito.mock(AuthService.class);
        PreferencesMapper mapper = Mockito.mock(PreferencesMapper.class);
        NotificationPreferenceRepository notificationPrefRepo = Mockito.mock(NotificationPreferenceRepository.class);

        service = new PreferencesService(authService, mapper, notificationPrefRepo);
    }

    @Test
    @DisplayName("ensureNotificationPrefs(User) - should return existing and generated preferences")
    void ensureNotificationPrefs01() {
        User user = Mockito.mock(User.class);
        NotificationPreference pref1 = new NotificationPreference();
        pref1 .setChannel(Channel.EMAIL);
        pref1.setType(NotificationType.APPROVAL);
        pref1.setUser(user);

        NotificationPreference pref2 = new NotificationPreference();
        pref2.setChannel(Channel.IN_APP);
        pref2.setType(NotificationType.APPROVAL);
        pref2.setUser(user);

        List<NotificationPreference> existingPreferences = List.of(pref1, pref2);
        when(user.getNotificationPreferences()).thenReturn(existingPreferences);

        List<NotificationPreference> result = service.ensureNotificationPrefs(user);

        assertEquals(12, result.size());
        assertEquals(existingPreferences.getFirst(), result.getFirst());
        assertEquals(existingPreferences.get(1), result.get(1));

        boolean hasMultipleEmailApproval = result.stream()
                .filter(p -> p.getChannel() == Channel.EMAIL && p.getType() == NotificationType.APPROVAL)
                .count() > 1;
        assertFalse(hasMultipleEmailApproval);
    }

    @Test
    @DisplayName("ensureNotificationPrefs(User) - should generate defaults")
    void ensureNotificationPrefs02() {
        User user = Mockito.mock(User.class);
        user.setNotificationPreferences(List.of());

        List<NotificationPreference> result = service.ensureNotificationPrefs(user);
        assertEquals(12, result.size());

        checkPref(result.getFirst(), Channel.IN_APP, NotificationType.REMINDER);
        checkPref(result.get(1), Channel.IN_APP, NotificationType.NEW_GAME);
        checkPref(result.get(2), Channel.IN_APP, NotificationType.NEW_RESULT);
        checkPref(result.get(3), Channel.IN_APP, NotificationType.APPROVAL);
        checkPref(result.get(4), Channel.IN_APP, NotificationType.REJECTION);
        checkPref(result.get(5), Channel.IN_APP, NotificationType.RANKING_UPDATE);
        checkPref(result.get(6), Channel.EMAIL, NotificationType.REMINDER);
        checkPref(result.get(7), Channel.EMAIL, NotificationType.NEW_GAME);
        checkPref(result.get(8), Channel.EMAIL, NotificationType.NEW_RESULT);
        checkPref(result.get(9), Channel.EMAIL, NotificationType.APPROVAL);
        checkPref(result.get(10), Channel.EMAIL, NotificationType.REJECTION);
        checkPref(result.get(11), Channel.EMAIL, NotificationType.RANKING_UPDATE);
    }

    void checkPref(NotificationPreference preference, Channel channel, NotificationType type) {
        assertEquals(channel, preference.getChannel());
        assertEquals(type, preference.getType());
    }
}
