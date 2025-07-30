package ch.no1hardy.service.service;

import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LeaderboardServiceTest {
    @Autowired
    private LeaderboardService service;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCalculateLeaderboard() {
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(100);
        user1.setLastReviewedPoints(50);
        user1.confirm();

        User user2 = new User();
        user2.setId("user-2");
        user2.setPoints(200);
        user2.setLastReviewedPoints(150);
        user2.confirm();

        User user3 = new User();
        user3.setId("user-3");
        user3.setPoints(200);
        user3.setLastReviewedPoints(100);
        user3.confirm();

        User user4 = new User();
        user4.setId("user-4");
        user4.setPoints(150);
        user4.setLastReviewedPoints(0);
        user4.confirm();

        when(userService.listConfirmedRaw()).thenReturn(List.of(user1, user2, user3, user4));

        List<RankingRes> res = service.getLeaderboard();

        assertEquals(user1.getId(), res.get(3).getId());
        assertEquals(user2.getId(), res.get(0).getId());
        assertEquals(user3.getId(), res.get(1).getId());
        assertEquals(user4.getId(), res.get(2).getId());
    }
}
