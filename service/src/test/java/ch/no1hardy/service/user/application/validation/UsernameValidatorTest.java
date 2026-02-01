package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.RequiredValidator;
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
    private RequiredValidator requiredValidator;

    @BeforeEach
    void setUp() {
        usernameAvailabilityService = Mockito.mock(UsernameAvailabilityService.class);
        Mockito.doReturn(true).when(usernameAvailabilityService).isAvailable("available1234");
        Mockito.doReturn(false).when(usernameAvailabilityService).isAvailable("taken1234");

        requiredValidator = Mockito.mock(RequiredValidator.class);
        Mockito.doReturn(false).when(requiredValidator).isBlank("available1234");
        Mockito.doReturn(true).when(requiredValidator).isBlank(null);

        validator = new UsernameValidator(usernameAvailabilityService, requiredValidator);
    }

    @Test
    @DisplayName("validate(String) - should pass when username is valid and available")
    void validate01() {
        assertDoesNotThrow(() -> validator.validate("available1234"));
        Mockito.verify(usernameAvailabilityService).isAvailable("available1234");
        Mockito.verify(requiredValidator).isBlank("available1234");
    }

    @Test
    @DisplayName("validate(String) - should not call availability service when RequiredValidator::isBlank returns true")
    void validate02() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(null));
        assertEquals("username is required", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(null);
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable(Mockito.anyString());
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is taken")
    void validate03() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate("taken1234"));
        assertEquals("username is already taken", e.getMessage());
        Mockito.verify(usernameAvailabilityService).isAvailable("taken1234");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when username is too short")
    void validate04() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate("ab"));
        assertEquals("username must be at least 3 characters long", e.getMessage());
        Mockito.verify(usernameAvailabilityService, Mockito.never()).isAvailable("ab");
    }
}
