package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import ch.no1hardy.service.user.domain.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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
        Email email = new Email("available@email.com", Optional.empty());
        assertDoesNotThrow(() -> validator.validate(email));
        Mockito.verify(requiredValidator).isBlank(email.address());
        Mockito.verify(emailAvailabilityService).isAvailable(email.address());
    }

    @Test
    @DisplayName("validate(Email) - should not call availability service when RequiredValidator::isBlank returns true")
    void validate02() {
        Email email = new Email(null, Optional.empty());
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(email));
        assertEquals("email is required", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(null);
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(Mockito.anyString());
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when email is taken")
    void validate05() {
        Email email = new Email("taken@email.com");
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(email));
        assertEquals("email is already taken", e.getMessage());
        Mockito.verify(emailAvailabilityService).isAvailable(email.address());
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when email object is null")
    void validate06() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(null));
        assertEquals("email is required", e.getMessage());
        Mockito.verify(requiredValidator, Mockito.never()).isBlank(Mockito.anyString());
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(Mockito.anyString());
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when email format is invalid")
    void validate07() {
        Email email = new Email("invalid-email-format");
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(email));
        assertEquals("invalid email format", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(email.address());
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email.address());

        Email email2 = new Email("user@.com");
        UserValidationException e2 = assertThrows(UserValidationException.class, () -> validator.validate(email2));
        assertEquals("invalid email format", e2.getMessage());
        Mockito.verify(requiredValidator).isBlank(email2.address());
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email2.address());

        Email email3 = new Email("user@domain");
        UserValidationException e3 = assertThrows(UserValidationException.class, () -> validator.validate(email3));
        assertEquals("invalid email format", e3.getMessage());
        Mockito.verify(requiredValidator).isBlank(email3.address());
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email3.address());

        Email email4 = new Email("user@domain.c");
        UserValidationException e4 = assertThrows(UserValidationException.class, () -> validator.validate(email4));
        assertEquals("invalid email format", e4.getMessage());
        Mockito.verify(requiredValidator).isBlank(email4.address());
        Mockito.verify(emailAvailabilityService, Mockito.never()).isAvailable(email4.address());
    }
}
