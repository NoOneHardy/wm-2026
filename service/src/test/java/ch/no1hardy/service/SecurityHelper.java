package ch.no1hardy.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nullable;
import java.util.Optional;

import static org.mockito.Mockito.when;

public abstract class SecurityHelper {
    public static void mockUserLogin(@Nullable User user) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext context = Mockito.mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        if (user != null) {
            UserRepository userRepository = Mockito.mock(UserRepository.class);
            when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        }
        SecurityContextHolder.setContext(context);

    }
}
