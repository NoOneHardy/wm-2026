package ch.no1hardy.service.user.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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
    private UserApplicationStatus userApplicationStatus = UserApplicationStatus.PENDING;
    private LocalDateTime applicationReviewedAt;

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = new Email(email, Optional.empty());
    }

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public Optional<LocalDateTime> getApplicationReviewedAt() {
        return Optional.ofNullable(applicationReviewedAt);
    }

    public boolean isApplicationReviewed() {
        return getApplicationReviewedAt().isPresent();
    }

    public void setApplicationReviewed(boolean applicationReviewed) {
        if (applicationReviewed && !isApplicationReviewed()) setApplicationReviewedAt(LocalDateTime.now());
        if (!applicationReviewed && isApplicationReviewed()) setApplicationReviewedAt(null);
    }

    public boolean isAccepted() {
        return getUserApplicationStatus().equals(UserApplicationStatus.ACCEPTED) && isApplicationReviewed();
    }

    public boolean isDenied() {
        return getUserApplicationStatus().equals(UserApplicationStatus.DENIED) && isApplicationReviewed();
    }

    public void approve() {
        if (isAccepted()) return;

        setApplicationReviewed(true);
        setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
    }

    public void deny() {
        if (isDenied()) return;

        setApplicationReviewed(true);
        setUserApplicationStatus(UserApplicationStatus.DENIED);
    }

    public void verifyEmail() {
        setEmail(email.verify());
    }
}
