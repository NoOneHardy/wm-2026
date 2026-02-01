package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorTest {
    private EmailValidator validator;
    private EmailAvailabilityService emailAvailabilityService;
    private RequiredValidator requiredValidator;

    @BeforeEach
    void setUp() {
        emailAvailabilityService = Mockito.mock(EmailAvailabilityService.class);
        Mockito.doReturn(true).when(emailAvailabilityService).isAvailable("available@email.com");
        Mockito.doReturn(false).when(emailAvailabilityService).isAvailable("taken@email.com");

        requiredValidator = Mockito.mock(RequiredValidator.class);
        Mockito.doReturn(false).when(requiredValidator).isBlank("available@email.com");
        Mockito.doReturn(true).when(requiredValidator).isBlank(null);

        validator = new EmailValidator(emailAvailabilityService, requiredValidator);
    }

    @Test
    @DisplayName("validate(Email) - should pass when email is valid and available")
    void validate01() {
        String email = "available@email.com";
        assertDoesNotThrow(() -> validator.validate(email));
        Mockito.verify(requiredValidator).isBlank(email);
        Mockito.verify(emailAvailabilityService).isAvailable(email);
    }

    @Test
    @DisplayName("validate(Email) - should not call availability service when RequiredValidator::isBlank returns true")
    void validate02() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(null));
        assertEquals("email is required", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(null);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(Mockito.anyString());
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when email is taken")
    void validate05() {
        String email = "taken@email.com";
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(email));
        assertEquals("email is already taken", e.getMessage());
        Mockito.verify(emailAvailabilityService).isAvailable(email);
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when email format is invalid")
    void validate07() {
        String email = "invalid-email-format";
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(email));
        assertEquals("invalid email format", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(email);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email);

        String email2 = "user@.com";
        UserValidationException e2 = assertThrows(UserValidationException.class, () -> validator.validate(email2));
        assertEquals("invalid email format", e2.getMessage());
        Mockito.verify(requiredValidator).isBlank(email2);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email2);

        String email3 = "user@domain";
        UserValidationException e3 = assertThrows(UserValidationException.class, () -> validator.validate(email3));
        assertEquals("invalid email format", e3.getMessage());
        Mockito.verify(requiredValidator).isBlank(email3);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email3);

        String email4 = "user@domain.c";
        UserValidationException e4 = assertThrows(UserValidationException.class, () -> validator.validate(email4));
        assertEquals("invalid email format", e4.getMessage());
        Mockito.verify(requiredValidator).isBlank(email4);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email4);
    }
}
