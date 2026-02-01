package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.port.out.UserReadRepositoryPort;
import ch.no1hardy.service.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameAvailabilityServiceTest {
    private UsernameAvailabilityService service;

    @BeforeEach
    void setUp() {
        User user = new User();
        UserReadRepositoryPort readRepositoryPort = Mockito.mock(UserReadRepositoryPort.class);
        Mockito.doReturn(Optional.of(user)).when(readRepositoryPort).findByUsername("taken1234");
        Mockito.doReturn(Optional.empty()).when(readRepositoryPort).findByUsername("available1234");

        service = new UsernameAvailabilityService(readRepositoryPort);
    }

    @Test
    @DisplayName("isAvailable(String) - should return false when username is taken")
    void isAvailable01() {
        assertFalse(service.isAvailable("taken1234"));
    }

    @Test
    @DisplayName("isAvailable(String) - should return true when username is available")
    void isAvailable02() {
        assertTrue(service.isAvailable("available1234"));
    }
}
