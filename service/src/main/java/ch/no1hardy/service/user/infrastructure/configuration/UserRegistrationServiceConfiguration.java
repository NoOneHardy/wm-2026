package ch.no1hardy.service.user.infrastructure.configuration;

import ch.no1hardy.service.user.application.port.out.PasswordHasher;
import ch.no1hardy.service.user.application.port.out.UserProfileMapper;
import ch.no1hardy.service.user.application.port.out.UserRegistrationMapper;
import ch.no1hardy.service.user.application.port.out.UserWriteRepository;
import ch.no1hardy.service.user.application.service.UserRegistrationService;
import ch.no1hardy.service.user.application.validation.UserRegistrationValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegistrationServiceConfiguration {
    @Bean
    public UserRegistrationService userRegistrationService(
            UserRegistrationValidator userRegistrationValidator,
            PasswordHasher passwordHasher,
            UserWriteRepository userWriteRepository,
            UserRegistrationMapper userRegistrationMapper,
            UserProfileMapper userProfileMapper

    ) {
        return new UserRegistrationService(
                userRegistrationValidator,
                passwordHasher,
                userWriteRepository,
                userRegistrationMapper,
                userProfileMapper
        );
    }
}
