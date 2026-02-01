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

public class EmailAvailabilityServiceTest {
    private EmailAvailabilityService service;

    @BeforeEach
    void setUp() {
        User user = new User();
        UserReadRepositoryPort readRepositoryPort = Mockito.mock(UserReadRepositoryPort.class);
        Mockito.doReturn(Optional.of(user)).when(readRepositoryPort).findByEmail("taken@email.com");
        Mockito.doReturn(Optional.empty()).when(readRepositoryPort).findByEmail("available@email.com");

        service = new EmailAvailabilityService(readRepositoryPort);
    }

    @Test
    @DisplayName("isAvailable(String) - should return false when email is taken")
    void isAvailable01() {
        assertFalse(service.isAvailable("taken@email.com"));
    }

    @Test
    @DisplayName("isAvailable(String) - should return true when email is available")
    void isAvailable02() {
        assertTrue(service.isAvailable("available@email.com"));
    }
}
