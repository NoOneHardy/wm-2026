package ch.no1hardy.service.user.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    @DisplayName("getAvatarUrl() - should return avatarUrl if set")
    void getAvatarUrl01() {
        User user = new User("id", "someemail");
        user.setAvatarUrl("https://example.com/avatar.png");
        assertTrue(user.getAvatarUrl().isPresent());
        assertEquals("https://example.com/avatar.png", user.getAvatarUrl().get());
    }

    @Test
    @DisplayName("getAvatarUrl() - should return empty if avatarUrl is not set")
    void getAvatarUrl02() {
        User user = new User("id", "someemail");
        assertTrue(user.getAvatarUrl().isEmpty());
    }

    @Test
    @DisplayName("getApplicationReviewedAt() - should return applicationReviewedAt if set")
    void getApplicationReviewedAt01() {
        User user = new User("id", "someemail");
        LocalDateTime applicationReviewedAt = LocalDateTime.now();
        user.setApplicationReviewedAt(applicationReviewedAt);
        assertTrue(user.getApplicationReviewedAt().isPresent());
        assertEquals(applicationReviewedAt, user.getApplicationReviewedAt().get());
    }

    @Test
    @DisplayName("getApplicationReviewedAt() - should return empty if applicationReviewedAt is not set")
    void getApplicationReviewedAt02() {
        User user = new User("id", "someemail");
        assertTrue(user.getApplicationReviewedAt().isEmpty());
    }

    @Test
    @DisplayName("getEmailVerifiedAt() - should return emailVerifiedAt if set")
    void getEmailVerifiedAt01() {
        User user = new User("id", "someemail");
        LocalDateTime emailVerifiedAt = LocalDateTime.now();
        user.setEmailVerifiedAt(emailVerifiedAt);
        assertTrue(user.getEmailVerifiedAt().isPresent());
        assertEquals(emailVerifiedAt, user.getEmailVerifiedAt().get());
    }

    @Test
    @DisplayName("getEmailVerifiedAt() - should return empty if emailVerifiedAt is not set")
    void getEmailVerifiedAt02() {
        User user = new User("id", "someemail");
        assertTrue(user.getEmailVerifiedAt().isEmpty());
    }

    @Test
    @DisplayName("isApplicationReviewed() - should return true if applicationReviewedAt is set")
    void isApplicationReviewed01() {
        User user = new User("id", "someemail");
        user.setApplicationReviewedAt(LocalDateTime.now());
        assertTrue(user.isApplicationReviewed());
    }

    @Test
    @DisplayName("isAccepted() - should return true if userApplicationStatus is ACCEPTED and applicationReviewed is true")
    void isAccepted01() {
        User user = new User("id", "someemail");
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setApplicationReviewed(true);
        assertTrue(user.isAccepted());
    }

    @Test
    @DisplayName("isAccepted() - should return false if userApplicationStatus is not ACCEPTED")
    void isAccepted02() {
        User user = new User("id", "someemail");
        user.setApplicationReviewed(true);
        assertFalse(user.isAccepted());
    }

    @Test
    @DisplayName("isAccepted() - should return false if applicationReviewedAt is false")
    void isAccepted03() {
        User user = new User("id", "someemail");
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        assertFalse(user.isAccepted());
    }

    @Test
    @DisplayName("isDenied() - should return true if userApplicationStatus is DENIED and applicationReviewed is true")
    void isDenied01() {
        User user = new User("id", "someemail");
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        user.setApplicationReviewed(true);
        assertTrue(user.isDenied());
    }

    @Test
    @DisplayName("isDenied() - should return false if userApplicationStatus is not DENIED")
    void isDenied02() {
        User user = new User("id", "someemail");
        user.setApplicationReviewed(true);
        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("isDenied() - should return false if applicationReviewed is false")
    void isDenied03() {
        User user = new User("id", "someemail");
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        assertFalse(user.isDenied());
    }

    @Test
    @DisplayName("approve() - should set userApplicationStatus to PENDING and applicationReviewed to true")
    void approve01() {
        User user = new User("id", "someemail");
        user.approve();
        assertEquals(UserApplicationStatus.ACCEPTED, user.getUserApplicationStatus());
        assertTrue(user.isApplicationReviewed());
    }

    @Test
    @DisplayName("approve() - should do nothing if userApplicationStatus is already ACCEPTED")
    void approve02() {
        User user = Mockito.spy(new User("id", "someemail"));
        user.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
        user.setApplicationReviewed(true);
        user.approve();

        Mockito.verify(user, Mockito.times(1)).setApplicationReviewed(true);
        Mockito.verify(user, Mockito.times(1)).setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
    }

    @Test
    @DisplayName("deny() - should set userApplicationStatus to DENIED and applicationReviewed to true")
    void deny01() {
        User user = new User("id", "someemail");
        user.deny();
        assertEquals(UserApplicationStatus.DENIED, user.getUserApplicationStatus());
        assertTrue(user.isApplicationReviewed());
    }

    @Test
    @DisplayName("deny() - should do nothing if userApplicationStatus is already DENIED")
    void deny02() {
        User user = Mockito.spy(new User("id", "someemail"));
        user.setUserApplicationStatus(UserApplicationStatus.DENIED);
        user.setApplicationReviewed(true);
        user.deny();

        Mockito.verify(user, Mockito.times(1)).setApplicationReviewed(true);
        Mockito.verify(user, Mockito.times(1)).setUserApplicationStatus(UserApplicationStatus.DENIED);
    }
}
