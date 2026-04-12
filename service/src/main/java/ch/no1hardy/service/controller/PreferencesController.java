package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.preferences.NotificationPreferenceReq;
import ch.no1hardy.service.front.preferences.NotificationPreferenceRes;
import ch.no1hardy.service.service.PreferencesService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/preferences")
@AllArgsConstructor
public class PreferencesController {
    private final PreferencesService preferencesService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notifications")
    public Optional<List<NotificationPreferenceRes>> getNotificationPreferences() {
        return preferencesService.getNotificationPrefs();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/notifications")
    public Optional<List<NotificationPreferenceRes>> updateNotificationPreferences(
            @RequestBody List<NotificationPreferenceReq> prefs
    ) {
        return preferencesService.updateNotificationPrefs(prefs);
    }
}
