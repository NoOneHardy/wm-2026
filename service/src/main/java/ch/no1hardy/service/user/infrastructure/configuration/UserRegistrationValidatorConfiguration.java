package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.user.application.validation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegistrationValidatorConfiguration {
    @Bean
    public UserRegistrationValidator userRegistrationValidator(
            UsernameValidator usernameValidator,
            EmailValidator emailValidator,
            PasswordValidator passwordValidator,
            FirstnameValidator firstnameValidator,
            LastnameValidator lastnameValidator
    ) {
        return new UserRegistrationValidator(
                usernameValidator,
                emailValidator,
                passwordValidator,
                firstnameValidator,
                lastnameValidator
        );
    }
}
