package ch.no1hardy.service.front;

import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.model.user.Role;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserRes record.
 * Verifies that the record behaves correctly as an immutable data carrier.
 */
class UserResRecordTest {

    @Test
    void shouldCreateRecordWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        UserRes user = new UserRes(
            "user-1",
            "testuser",
            "test@example.com",
            "John",
            "Doe",
            100,
            80,
            "avatar-url",
            Role.USER,
            now,
            UserApplicationStatus.ACCEPTED
        );
        
        assertEquals("user-1", user.id());
        assertEquals("testuser", user.username());
        assertEquals("test@example.com", user.email());
        assertEquals("John", user.firstname());
        assertEquals("Doe", user.lastname());
        assertEquals(100, user.points());
        assertEquals(80, user.lastReviewedPoints());
        assertEquals("avatar-url", user.avatarUrl());
        assertEquals(Role.USER, user.role());
        assertEquals(now, user.applicationReviewedAt());
        assertEquals(UserApplicationStatus.ACCEPTED, user.userApplicationStatus());
    }

    @Test
    void shouldSupportEquality() {
        LocalDateTime now = LocalDateTime.now();
        UserRes user1 = new UserRes("user-1", "test", "test@ex.com", "John", "Doe", 
                                     100, 80, "avatar", Role.USER, now, UserApplicationStatus.ACCEPTED);
        UserRes user2 = new UserRes("user-1", "test", "test@ex.com", "John", "Doe", 
                                     100, 80, "avatar", Role.USER, now, UserApplicationStatus.ACCEPTED);
        
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
