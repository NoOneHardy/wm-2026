package ch.no1hardy.service;

import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.mapper.UserMapperImpl;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import ch.no1hardy.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {
    @MockitoBean
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Autowired
    private UserMapperImpl mapper;

    @Test
    void contextLoads() {
        assertThat(service).isNotNull();
    }

    @Test
    void listAll() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        assertThat(service.listAll()).isNotNull();
        assertThat(service.listAll()).hasSize(2);
        assertThat(service.listAll()).contains(mapper.toDto(user1), mapper.toDto(user2));
    }

    @Test
    void list() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        assertThat(service.list()).isNotNull();
        assertThat(service.list()).hasSize(1);
        assertThat(service.list()).contains(mapper.toDto(user1));
        assertThat(service.list()).doesNotContain(mapper.toDto(user2));
    }

    @Test
    void check() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@no1hardy.ch");

        when(repository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(repository.findByEmail("user1@no1hardy.ch")).thenReturn(Optional.of(user1));

        // Username without email
        CheckRes usernameWithoutEmail = service.check("user1", null);
        assertThat(usernameWithoutEmail).isNotNull();
        assertThat(usernameWithoutEmail.getIsUsernameAvailable()).isFalse();
        assertThat(usernameWithoutEmail.getIsEmailAvailable()).isTrue();

        // Email without username
        CheckRes emailWithoutUsername = service.check(null, "user1@no1hardy.ch");
        assertThat(emailWithoutUsername).isNotNull();
        assertThat(emailWithoutUsername.getIsUsernameAvailable()).isTrue();
        assertThat(emailWithoutUsername.getIsEmailAvailable()).isFalse();

        // Email with username
        CheckRes emailWithUsername = service.check("user1", "user1@no1hardy.ch");
        assertThat(emailWithUsername).isNotNull();
        assertThat(emailWithUsername.getIsUsernameAvailable()).isFalse();
        assertThat(emailWithUsername.getIsEmailAvailable()).isFalse();
    }

    @Test
    void isUsernameAvailable() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(repository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(repository.findByUsername("user3")).thenReturn(Optional.empty());

        // Existing username
        assertThat(service.isUsernameAvailable("user1")).isFalse();

        // Deleted username
        assertThat(service.isUsernameAvailable("user2")).isTrue();

        // New username
        assertThat(service.isUsernameAvailable("user3")).isTrue();
    }

    @Test
    void isEmailAvailable() {
        User user1 = new User();
        user1.setEmail("user1@no1hardy.ch");
        User user2 = new User();
        user2.setEmail("user2@no1hardy.ch");
        user2.setDeletedAt(LocalDateTime.now());

        when(repository.findByEmail("user1@no1hardy.ch")).thenReturn(Optional.of(user1));
        when(repository.findByEmail("user2@no1hardy.ch")).thenReturn(Optional.of(user2));
        when(repository.findByEmail("user3@no1hardy.ch")).thenReturn(Optional.empty());

        // Existing email
        assertThat(service.isEmailAvailable("user1@no1hardy.ch")).isFalse();

        // Deleted email
        assertThat(service.isEmailAvailable("user2@no1hardy.ch")).isTrue();

        // New email
        assertThat(service.isEmailAvailable("user3@no1hardy.ch")).isTrue();
    }
}
