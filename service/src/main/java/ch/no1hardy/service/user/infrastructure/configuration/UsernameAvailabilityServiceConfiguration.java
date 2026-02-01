package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.user.application.port.out.UserReadRepository;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsernameAvailabilityServiceConfiguration {
    @Bean
    public UsernameAvailabilityService usernameAvailabilityService(
            UserReadRepository userReadRepository
    ) {
        return new UsernameAvailabilityService(userReadRepository);
    }
}
