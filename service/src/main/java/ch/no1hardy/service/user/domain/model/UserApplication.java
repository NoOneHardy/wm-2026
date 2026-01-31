package ch.no1hardy.service.user.domain.model;

import java.time.LocalDateTime;
import java.util.Optional;

public record UserApplication(
        UserApplicationStatus status,
        Optional<LocalDateTime> processedAt
) {
    public static UserApplication pending() {
        return new UserApplication(
                UserApplicationStatus.PENDING,
                Optional.empty()
        );
    }

    public boolean isProcessed() {
        return processedAt.isPresent();
    }

    public boolean isAccepted() {
        return UserApplicationStatus.ACCEPTED.equals(status) && isProcessed();
    }

    public boolean isDenied() {
        return UserApplicationStatus.DENIED.equals(status) && isProcessed();
    }

    public UserApplication accept() {
        if (isAccepted()) return this;
        return new UserApplication(
                UserApplicationStatus.ACCEPTED,
                Optional.of(LocalDateTime.now())
        );
    }

    public UserApplication deny() {
        if (isDenied()) return this;
        return new UserApplication(
                UserApplicationStatus.DENIED,
                Optional.of(LocalDateTime.now())
        );
    }
}
