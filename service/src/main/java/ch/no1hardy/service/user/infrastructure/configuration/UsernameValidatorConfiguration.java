package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import ch.no1hardy.service.user.application.validation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsernameValidatorConfiguration {
    @Bean
    public UsernameValidator usernameValidator(
            UsernameAvailabilityService availabilityService,
            RequiredValidator requiredValidator
    ) {
        return new UsernameValidator(
                availabilityService,
                requiredValidator
        );
    }
}
