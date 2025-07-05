package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("getDashboard() - should return dashboard data with all fields populated")
    void shouldReturnDashboardDataWithAllFieldsPopulated() {
        when(userService.getUserLeaderboard()).thenReturn(List.of(
                RankingRes.builder().id("user-1").ranking(1).points(1200).build()
        ));
        when(userService.getUserSummary()).thenReturn(UserSummary.builder()
                .ranking(1)
                .isConfirmed(true)
                .points(1200)
                .percentage(85.0)
                .build());
        when(gameService.getOpenBets()).thenReturn(List.of(
                BetGameRes.builder().id("game-1").build()
        ));
        when(userService.getStatistics()).thenReturn(Statistics.builder()
                .totalGoalsBet(10)
                .correctGames(5)
                .jokersWasted(2)
                .build());
        User user1 = new User();
        user1.setId("user-1");
        user1.setPoints(1200);
        User user2 = new User();
        user2.setId("user-2");
        user2.setPoints(800);
        when(userService.listRaw()).thenReturn(List.of(user1, user2));
        when(userService.getCorrectGames(ArgumentMatchers.any(User.class))).thenReturn(3);
        when(userService.getJokersWasted(ArgumentMatchers.any(User.class))).thenReturn(1);

        DashboardData dashboardData = service.getDashboard();

        assertEquals(1, dashboardData.getLeaderboardPreview().size());
        assertTrue(dashboardData.getUserSummary().getIsConfirmed());
        assertEquals(1200, dashboardData.getUserSummary().getPoints());
        assertEquals(1, dashboardData.getUserSummary().getRanking());
        assertEquals(85.0, dashboardData.getUserSummary().getPercentage());
        assertEquals(1, dashboardData.getOpenBets().size());
        assertEquals("game-1", dashboardData.getOpenBets().getFirst().getId());
        assertEquals(10, dashboardData.getStats().getTotalGoalsBet());
        assertEquals(5, dashboardData.getStats().getCorrectGames());
        assertEquals(2, dashboardData.getStats().getJokersWasted());
        assertEquals(2000, dashboardData.getGlobalStats().getTotalPoints());
        assertEquals(6, dashboardData.getGlobalStats().getCorrectGames());
        assertEquals(2, dashboardData.getGlobalStats().getJokersWasted());
    }

    @Test
    @DisplayName("getGlobalStatistics() - should return correct aggregated statistics for all users")
    void shouldReturnCorrectAggregatedStatisticsForAllUsers() {
        User user1 = new User();
        user1.setPoints(1200);
        User user2 = new User();
        user2.setPoints(800);
        when(userService.listRaw()).thenReturn(List.of(user1, user2));
        when(userService.getCorrectGames(user1)).thenReturn(3);
        when(userService.getCorrectGames(user2)).thenReturn(2);
        when(userService.getJokersWasted(user1)).thenReturn(1);
        when(userService.getJokersWasted(user2)).thenReturn(2);

        GlobalStatistics globalStats = service.getGlobalStatistics();

        assertEquals(2000, globalStats.getTotalPoints());
        assertEquals(5, globalStats.getCorrectGames());
        assertEquals(3, globalStats.getJokersWasted());
    }

    @Test
    @DisplayName("getGlobalStatistics() - should return zero statistics if no users exist")
    void shouldReturnZeroStatisticsIfNoUsersExist() {
        when(userService.listRaw()).thenReturn(List.of());

        GlobalStatistics globalStats = service.getGlobalStatistics();

        assertEquals(0, globalStats.getTotalPoints());
        assertEquals(0, globalStats.getCorrectGames());
        assertEquals(0, globalStats.getJokersWasted());
    }
}
