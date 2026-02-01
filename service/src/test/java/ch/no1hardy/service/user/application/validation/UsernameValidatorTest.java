package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameValidatorTest {
    private UsernameValidator validator;
    private UsernameAvailabilityService usernameAvailabilityService;

    @BeforeEach
    void setUp() {
        usernameAvailabilityService = Mockito.mock(UsernameAvailabilityService.class);
        Mockito.doReturn(true).when(usernameAvailabilityService).isAvailable("available1234");
        Mockito.doReturn(false).when(usernameAvailabilityService).isAvailable("taken1234");

        validator = new UsernameValidator(usernameAvailabilityService);
    }

    @Test
    @DisplayName("validate(String) - should pass when username is valid and available")
    void validate01() {
        assertDoesNotThrow(() -> validator.validate("available1234"));
        Mockito.verify(usernameAvailabilityService).isAvailable("available1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is empty")
    void validate02() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(""));
        assertEquals("username is required", e.getMessage());
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable("available1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is null")
    void validate03() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(null));
        assertEquals("username is required", e.getMessage());
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable("available1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is blank")
    void validate04() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate("   "));
        assertEquals("username is required", e.getMessage());
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable("available1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is taken")
    void validate05() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate("taken1234"));
        assertEquals("username is already taken", e.getMessage());
        Mockito.verify(usernameAvailabilityService).isAvailable("taken1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is too short")
    void validate06() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate("ab"));
        assertEquals("username must be at least 3 characters long", e.getMessage());
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable("ab");
    }
}
