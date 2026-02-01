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
    private PasswordValidator passwordValidator;
    private FirstnameValidator firstnameValidator;

    @BeforeEach
    void setUp() {
        usernameValidator = Mockito.mock(UsernameValidator.class);
        emailValidator = Mockito.mock(EmailValidator.class);
        passwordValidator = Mockito.mock(PasswordValidator.class);
        firstnameValidator = Mockito.mock(FirstnameValidator.class);

        Mockito.doNothing().when(usernameValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(emailValidator).validate(Mockito.any(Email.class));
        Mockito.doNothing().when(passwordValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(firstnameValidator).validate(Mockito.anyString());

        validator = new UserRegistrationValidator(usernameValidator, emailValidator, passwordValidator, firstnameValidator);
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

    @Test
    @DisplayName("validate() - should call password validator")
    void validate03() {
        User user = new User();
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(passwordValidator).validate(user.getPasswordHash());
        }
    }

    @Test
    @DisplayName("validate() - should call firstname validator")
    void validate04() {
        User user = new User();
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(firstnameValidator).validate(user.getFirstname());
        }
    }
}
