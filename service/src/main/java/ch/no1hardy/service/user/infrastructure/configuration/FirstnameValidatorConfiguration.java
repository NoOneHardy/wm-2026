package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.validation.FirstnameValidator;
import ch.no1hardy.service.user.application.validation.PasswordValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirstnameValidatorConfiguration {
    @Bean
    public FirstnameValidator firstnameValidator(
            RequiredValidator requiredValidator
    ) {
        return new FirstnameValidator(requiredValidator);
    }
}
