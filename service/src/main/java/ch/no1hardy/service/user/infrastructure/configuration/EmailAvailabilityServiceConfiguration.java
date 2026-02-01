package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.user.application.port.out.UserReadRepository;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailAvailabilityServiceConfiguration {
    @Bean
    public EmailAvailabilityService emailAvailabilityService(
            UserReadRepository userReadRepository
    ) {
        return new EmailAvailabilityService(userReadRepository);
    }
}
