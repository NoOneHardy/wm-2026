package ch.no1hardy.service.user.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserApplicationTest {
    @Test
    @DisplayName("pending() - should create UserApplication with PENDING status and empty processedAt")
    void pending01() {
        UserApplication application = UserApplication.pending();
        assertEquals(UserApplicationStatus.PENDING, application.status());
        assertTrue(application.processedAt().isEmpty());
    }

    @Test
    @DisplayName("isProcessed() - should return true if processedAt is set")
    void isProcessed01() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.of(LocalDateTime.now())
        );
        assertTrue(application.isProcessed());
    }

    @Test
    @DisplayName("isAccepted() - should return true if status is ACCEPTED and isProcessed is true")
    void isAccepted01() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.of(LocalDateTime.now())
        );
        assertTrue(application.isAccepted());
    }

    @Test
    @DisplayName("isAccepted() - should return false if status is not ACCEPTED")
    void isAccepted02() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.DENIED,
                Optional.of(LocalDateTime.now())
        );
        assertFalse(application.isAccepted());
    }

    @Test
    @DisplayName("isAccepted() - should return false if isProcessed is false")
    void isAccepted03() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.empty()
        );
        assertFalse(application.isAccepted());
    }

    @Test
    @DisplayName("isDenied() - should return true if status is DENIED and isProcessed is true")
    void isDenied01() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.DENIED,
                Optional.of(LocalDateTime.now())
        );
        assertTrue(application.isDenied());
    }

    @Test
    @DisplayName("isDenied() - should return false if status is not DENIED")
    void isDenied02() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.of(LocalDateTime.now())
        );
        assertFalse(application.isDenied());
    }

    @Test
    @DisplayName("isDenied() - should return false if isProcessed is false")
    void isDenied03() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.DENIED,
                Optional.empty()
        );
        assertFalse(application.isDenied());
    }

    @Test
    @DisplayName("accept() - should return new UserApplication with ACCEPTED status and current processedAt if not already accepted")
    void approve01() {
        UserApplication application = UserApplication.pending();
        UserApplication acceptedApplication = application.accept();

        assertNotSame(application, acceptedApplication);
        assertEquals(UserApplicationStatus.ACCEPTED, acceptedApplication.status());
        assertTrue(acceptedApplication.processedAt().isPresent());
    }

    @Test
    @DisplayName("accept() - should return same UserApplication if already accepted")
    void approve02() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.of(LocalDateTime.now())
        );
        UserApplication acceptedApplication = application.accept();

        assertSame(application, acceptedApplication);
    }

    @Test
    @DisplayName("deny() - should return new UserApplication with DENIED status and current processedAt if not already denied")
    void deny01() {
        UserApplication application = UserApplication.pending();
        UserApplication deniedApplication = application.deny();

        assertNotSame(application, deniedApplication);
        assertEquals(UserApplicationStatus.DENIED, deniedApplication.status());
        assertTrue(deniedApplication.processedAt().isPresent());
    }

    @Test
    @DisplayName("deny() - should return same UserApplication if already denied")
    void deny02() {
        UserApplication application = new UserApplication(
                UserApplicationStatus.DENIED,
                Optional.of(LocalDateTime.now())
        );
        UserApplication deniedApplication = application.deny();

        assertSame(application, deniedApplication);
    }
}
