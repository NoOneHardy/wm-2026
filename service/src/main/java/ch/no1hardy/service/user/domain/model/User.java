package ch.no1hardy.service.user.domain.model;

import ch.no1hardy.service.shared.UserId;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public final class User {
    private final UserId id;
    private Email email;
    private String username;
    private String passwordHash;
    private String firstname;
    private String lastname;
    private UserRole role = UserRole.USER;
    private String avatarUrl;
    private UserApplication userApplication = UserApplication.pending();

    public User() {
        this.id = new UserId(UUID.randomUUID());
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = new Email(email, Optional.empty());
    }

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public void accept() {
        setUserApplication(userApplication.accept());
    }

    public void deny() {
        setUserApplication(userApplication.deny());
    }

    public void verifyEmail() {
        setEmail(email.verify());
    }
}
