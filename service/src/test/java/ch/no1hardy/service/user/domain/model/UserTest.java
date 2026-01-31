package ch.no1hardy.service.user.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    @DisplayName("getAvatarUrl() - should return avatarUrl if set")
    void getAvatarUrl01() {
        User user = new User("id");
        user.setAvatarUrl("https://example.com/avatar.png");
        assertTrue(user.getAvatarUrl().isPresent());
        assertEquals("https://example.com/avatar.png", user.getAvatarUrl().get());
    }

    @Test
    @DisplayName("getAvatarUrl() - should return empty if avatarUrl is not set")
    void getAvatarUrl02() {
        User user = new User("id");
        assertTrue(user.getAvatarUrl().isEmpty());
    }

    @Test
    @DisplayName("verifyEmail() - should call Email::verify and replace user email")
    void verifyEmail01() {
        User user = new User("id");
        Email email = Mockito.spy(new Email("someemail", Optional.empty()));
        user.setEmail(email);
        user.verifyEmail();
        Mockito.verify(email, Mockito.times(1)).verify();
        assertNotSame(email, user.getEmail());
    }

    @Test
    @DisplayName("accept() - should call UserApplication::accept and replace user application")
    void accept01() {
        User user = new User("id");
        UserApplication application = Mockito.spy(UserApplication.pending());
        user.setUserApplication(application);
        user.accept();
        Mockito.verify(application, Mockito.times(1)).accept();
        assertNotSame(application, user.getUserApplication());
    }

    @Test
    @DisplayName("deny() - should call UserApplication::deny and replace user application")
    void deny01() {
        User user = new User("id");
        UserApplication application = Mockito.spy(UserApplication.pending());
        user.setUserApplication(application);
        user.deny();
        Mockito.verify(application, Mockito.times(1)).deny();
        assertNotSame(application, user.getUserApplication());
    }
}
