package ch.no1hardy.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import ch.no1hardy.service.service.AuthService;
import jakarta.validation.constraints.NotNull;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

public abstract class SecurityHelper {
    public static void mockUserLogin(AuthService service, @NotNull User user) {
        when(service.getLoggedInUser()).thenReturn(Optional.of(user));
    }

    public static void mockNoLogin(AuthService service) {

        when(service.getLoggedInUser()).thenReturn(Optional.empty());
    }
}
