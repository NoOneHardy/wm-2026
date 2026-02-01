package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.user.domain.model.Email;
import ch.no1hardy.service.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserRegistrationValidatorTest {
    private UserRegistrationValidator validator;
    private UsernameValidator usernameValidator;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        usernameValidator = Mockito.mock(UsernameValidator.class);
        emailValidator = Mockito.mock(EmailValidator.class);

        Mockito.doNothing().when(usernameValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(emailValidator).validate(Mockito.any(Email.class));

        validator = new UserRegistrationValidator(usernameValidator, emailValidator);
    }

    @Test
    @DisplayName("validate() - should call username validator")
    void validate01() {
        User user = new User();
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(usernameValidator).validate(user.getUsername());
        }
    }

    @Test
    @DisplayName("validate() - should call email validator")
    void validate02() {
        User user = new User();
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(emailValidator).validate(user.getEmail());
        }
    }
}
