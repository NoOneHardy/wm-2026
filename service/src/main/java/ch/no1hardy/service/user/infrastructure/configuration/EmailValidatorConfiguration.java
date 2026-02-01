package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import ch.no1hardy.service.user.application.validation.EmailValidator;
import ch.no1hardy.service.user.application.validation.UsernameValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailValidatorConfiguration {
    @Bean
    public EmailValidator emailValidator(
            EmailAvailabilityService availabilityService,
            RequiredValidator requiredValidator
    ) {
        return new EmailValidator(
                availabilityService,
                requiredValidator
        );
    }
}
