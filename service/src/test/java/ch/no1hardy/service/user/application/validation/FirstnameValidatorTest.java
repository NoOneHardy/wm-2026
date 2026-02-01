package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class FirstnameValidatorTest {
    private FirstnameValidator validator;
    private RequiredValidator requiredValidator;

    @BeforeEach
    void setUp() {
        requiredValidator = Mockito.mock(RequiredValidator.class);
        Mockito.doReturn(true).when(requiredValidator).isBlank(null);
        Mockito.doReturn(false).when(requiredValidator).isBlank("John");

        validator = new FirstnameValidator(requiredValidator);
    }

    @Test
    @DisplayName("validate(String) - should pass when firstname is valid")
    void validate01() {
        assertDoesNotThrow(() -> validator.validate("John"));
        Mockito.verify(requiredValidator).isBlank("John");
    }

    @Test
    @DisplayName("validate(String) - should throw UserValidationException when RequiredValidator::isBlank returns true")
    void validate02() {
        UserValidationException e = assertThrows(UserValidationException.class, () -> validator.validate(null));
        assertEquals("firstname is required", e.getMessage());
        Mockito.verify(requiredValidator).isBlank(null);
    }
}
