package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
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
    private LastnameValidator lastnameValidator;

    private UserRegistrationDto user;

    @BeforeEach
    void setUp() {
        usernameValidator = Mockito.mock(UsernameValidator.class);
        emailValidator = Mockito.mock(EmailValidator.class);
        passwordValidator = Mockito.mock(PasswordValidator.class);
        firstnameValidator = Mockito.mock(FirstnameValidator.class);
        lastnameValidator = Mockito.mock(LastnameValidator.class);

        Mockito.doNothing().when(usernameValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(emailValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(passwordValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(firstnameValidator).validate(Mockito.anyString());
        Mockito.doNothing().when(lastnameValidator).validate(Mockito.anyString());

        validator = new UserRegistrationValidator(usernameValidator, emailValidator, passwordValidator, firstnameValidator, lastnameValidator);
        user = new UserRegistrationDto("username", "password", "available@email.com", "firstname", "lastname");
    }

    @Test
    @DisplayName("validate() - should call username validator")
    void validate01() {
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(usernameValidator).validate(user.username());
        }
    }

    @Test
    @DisplayName("validate() - should call email validator")
    void validate02() {
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(emailValidator).validate(user.email());
        }
    }

    @Test
    @DisplayName("validate() - should call password validator")
    void validate03() {
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(passwordValidator).validate(user.password());
        }
    }

    @Test
    @DisplayName("validate() - should call firstname validator")
    void validate04() {
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(firstnameValidator).validate(user.firstname());
        }
    }

    @Test
    @DisplayName("validate() - should call lastname validator")
    void validate05() {
        try {
            validator.validate(user);
        } finally {
            Mockito.verify(lastnameValidator).validate(user.lastname());
        }
    }
}
