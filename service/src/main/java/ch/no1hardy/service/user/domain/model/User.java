package ch.no1hardy.service.user.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public final class User {
    private final String id;
    private final String email;
    private String username;
    private String firstname;
    private String lastname;
    private UserRole role = UserRole.USER;
    private String avatarUrl;
    private UserApplicationStatus userApplicationStatus = UserApplicationStatus.PENDING;
    private LocalDateTime applicationReviewedAt;
    private LocalDateTime emailVerifiedAt;

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public Optional<LocalDateTime> getApplicationReviewedAt() {
        return Optional.ofNullable(applicationReviewedAt);
    }

    public Optional<LocalDateTime> getEmailVerifiedAt() {
        return Optional.ofNullable(emailVerifiedAt);
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
}
