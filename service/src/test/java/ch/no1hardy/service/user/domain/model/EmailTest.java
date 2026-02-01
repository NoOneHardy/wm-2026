package ch.no1hardy.service.user.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {
    @Test
    @DisplayName("Email(String) - should create Email with empty verifiedAt")
    void Email01() {
        Email email = new Email("someemail");
        assertEquals("someemail", email.address());
        assertTrue(email.verifiedAt().isEmpty());
    }

    @Test
    @DisplayName("isVerified() - should return true if email is verified")
    void isVerified01() {
        LocalDateTime verifiedAt = LocalDateTime.now();
        Email email = new Email("someemail", Optional.of(verifiedAt));
        assertTrue(email.isVerified());
    }

    @Test
    @DisplayName("isVerified() - should return false if email is not verified")
    void isVerified02() {
        Email email = new Email("someemail");
        assertFalse(email.isVerified());
    }

    @Test
    @DisplayName("verify() - should return new Email with verifiedAt set if not already verified")
    void verify01() {
        Email email = new Email("someemail");
        Email verifiedEmail = email.verify();
        assertNotSame(email, verifiedEmail);
        assertTrue(verifiedEmail.isVerified());
        assertTrue(verifiedEmail.verifiedAt().isPresent());
    }

    @Test
    @DisplayName("verify() - should return same Email instance if already verified")
    void verify02() {
        LocalDateTime verifiedAt = LocalDateTime.now();
        Email email = new Email("someemail", Optional.of(verifiedAt));
        Email verifiedEmail = email.verify();
        assertSame(email, verifiedEmail);
        assertTrue(email.isVerified());
    }
}
