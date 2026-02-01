package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.validation.LastnameValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LastnameValidatorConfiguration {
    @Bean
    public LastnameValidator lastnameValidator(
            RequiredValidator requiredValidator
    ) {
        return new LastnameValidator(requiredValidator);
    }
}
