package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.shared.RequiredValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequiredValidatorConfiguration {
    @Bean
    public RequiredValidator requiredValidator() {
        return new RequiredValidator();
    }
}
