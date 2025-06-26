package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    private DashboardService service;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("should return dashboard data")
    public void shouldReturnDashboardData() {
        when(userService.getUserLeaderboard()).thenReturn(List.of(
                RankingRes.builder()
                        .id("user-1")
                        .ranking(2)
                        .points(1000)
                        .build(),
                RankingRes.builder()
                        .id("user-2")
                        .ranking(3)
                        .points(950)
                        .build(),
                RankingRes.builder()
                        .id("user-3")
                        .ranking(4)
                        .points(800)
                        .build()
        ));

        DashboardData dashboardData = service.getDashboard();

        assertEquals("user-1", dashboardData.getLeaderboardPreview().getFirst().getId());
        assertEquals("user-2", dashboardData.getLeaderboardPreview().get(1).getId());
        assertEquals("user-3", dashboardData.getLeaderboardPreview().get(2).getId());
    }
}
