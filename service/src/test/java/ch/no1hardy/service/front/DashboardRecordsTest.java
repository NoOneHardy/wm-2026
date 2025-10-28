package ch.no1hardy.service.front;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.front.dashboard.UserSummary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Dashboard-related records.
 * Verifies that the records behave correctly as immutable data carriers.
 */
class DashboardRecordsTest {

    @Test
    void shouldCreateGlobalStatisticsRecord() {
        GlobalStatistics stats = new GlobalStatistics(1000, 50, 10);
        
        assertEquals(1000, stats.totalPoints());
        assertEquals(50, stats.correctGames());
        assertEquals(10, stats.jokersWasted());
    }

    @Test
    void shouldCreateUserSummaryRecord() {
        UserSummary summary = new UserSummary(100, 5, 75.5, true);
        
        assertEquals(100, summary.points());
        assertEquals(5, summary.ranking());
        assertEquals(75.5, summary.percentage());
        assertTrue(summary.isConfirmed());
    }

    @Test
    void shouldCreateStatisticsRecord() {
        Statistics stats = new Statistics(25, 10, 2);
        
        assertEquals(25, stats.totalGoalsBet());
        assertEquals(10, stats.correctGames());
        assertEquals(2, stats.jokersWasted());
    }

    @Test
    void shouldCreateDashboardDataRecord() {
        UserSummary userSummary = new UserSummary(100, 5, 75.5, true);
        Statistics stats = new Statistics(25, 10, 2);
        GlobalStatistics globalStats = new GlobalStatistics(1000, 50, 10);
        
        DashboardData dashboard = new DashboardData(
            List.of(),
            userSummary,
            stats,
            globalStats,
            List.of(),
            List.of()
        );
        
        assertNotNull(dashboard.leaderboardPreview());
        assertEquals(userSummary, dashboard.userSummary());
        assertEquals(stats, dashboard.stats());
        assertEquals(globalStats, dashboard.globalStats());
        assertNotNull(dashboard.upcomingGames());
        assertNotNull(dashboard.recentResults());
    }

    @Test
    void shouldSupportEqualityForStatistics() {
        Statistics stats1 = new Statistics(25, 10, 2);
        Statistics stats2 = new Statistics(25, 10, 2);
        Statistics stats3 = new Statistics(30, 12, 3);
        
        assertEquals(stats1, stats2);
        assertNotEquals(stats1, stats3);
        assertEquals(stats1.hashCode(), stats2.hashCode());
    }
}
