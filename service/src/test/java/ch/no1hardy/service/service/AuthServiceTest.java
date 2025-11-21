package ch.no1hardy.service.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    private AuthService authService;
    private SecurityContext securityContext;
    private Authentication authentication;
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);
        userRepository = Mockito.mock(UserRepository.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        authService = new AuthService(userRepository);
    }

    @Test
    @DisplayName("boolean isAuthenticated() - should return false when no user is logged in")
    void isAuthenticated01() {
        when(securityContext.getAuthentication()).thenReturn(null);
        assertFalse(authService.isAuthenticated());
    }

    @Test
    @DisplayName("boolean isAuthenticated() - should return false when anonymous user is logged in")
    void isAuthenticated02() {
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        assertFalse(authService.isAuthenticated());
    }

    @Test
    @DisplayName("boolean isAuthenticated() - should return true when a user is logged in")
    void isAuthenticated03() {
        User user = new User();
        user.setId("test-user-id");
        when(authentication.getPrincipal()).thenReturn(user);

        assertTrue(authService.isAuthenticated());
    }

    @Test
    @DisplayName("Optional<User> getLoggedInUser() - should return empty when no user is logged in")
    void getLoggedInUser01() {
        when(securityContext.getAuthentication()).thenReturn(null);
        assertTrue(authService.getLoggedInUser().isEmpty());
    }

    @Test
    @DisplayName("Optional<User> getLoggedInUser() - should return empty when no user is logged in")
    void getLoggedInUser02() {
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        assertTrue(authService.getLoggedInUser().isEmpty());
    }

    @Test
    @DisplayName("Optional<User> getLoggedInUser() - should return user if user is logged in")
    void getLoggedInUser03() {
        User user = new User();
        user.setId("test-user-id");
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findById("test-user-id")).thenReturn(java.util.Optional.of(user));

        assertTrue(authService.getLoggedInUser().isPresent());
        assertEquals("test-user-id", authService.getLoggedInUser().get().getId());
    }

    @Test
    @DisplayName("Optional<User> getLoggedInUser() - should return empty if user somehow doesn't exist")
    void getLoggedInUser04() {
        User user = new User();
        user.setId("non-existent-user-id");
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findById("non-existent-user-id")).thenReturn(java.util.Optional.empty());

        assertTrue(authService.getLoggedInUser().isEmpty());
    }
}
