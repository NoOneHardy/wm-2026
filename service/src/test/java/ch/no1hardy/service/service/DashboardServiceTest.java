package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.game.BetGameRes;
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

    @MockitoBean
    private GameService gameService;

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
        when(userService.getUserSummary()).thenReturn(UserSummary.builder()
                .ranking(2)
                .isConfirmed(true)
                .points(1000)
                .percentage(70.0)
                .build());

        BetGameRes game = BetGameRes.builder().id("game-1").build();
        when(gameService.getOpenBets()).thenReturn(List.of(game));

        DashboardData dashboardData = service.getDashboard();

        assertEquals("user-1", dashboardData.getLeaderboardPreview().getFirst().getId());
        assertEquals("user-2", dashboardData.getLeaderboardPreview().get(1).getId());
        assertEquals("user-3", dashboardData.getLeaderboardPreview().get(2).getId());
        assertEquals(2, dashboardData.getUserSummary().getRanking());
        assertEquals(1000, dashboardData.getUserSummary().getPoints());
        assertEquals(70.0, dashboardData.getUserSummary().getPercentage());
        assertEquals(true, dashboardData.getUserSummary().getIsConfirmed());
    }
}
