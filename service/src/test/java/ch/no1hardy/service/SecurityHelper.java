package ch.no1hardy.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.when;

public abstract class SecurityHelper {
    public static void mockUserLogin(@NotNull User user, @NotNull UserRepository userRepository) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext context = Mockito.mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        SecurityContextHolder.setContext(context);
    }

    public static void mockNoLogin() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext context = Mockito.mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.setContext(context);
    }
}
