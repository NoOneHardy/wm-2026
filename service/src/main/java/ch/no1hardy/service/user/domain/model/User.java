package ch.no1hardy.service.user.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public final class User {
    private final String id;
    private Email email;
    private String username;
    private String firstname;
    private String lastname;
    private UserRole role = UserRole.USER;
    private String avatarUrl;
    private UserApplication userApplication = UserApplication.pending();

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
