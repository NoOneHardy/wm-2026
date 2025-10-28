package ch.no1hardy.service.front.dashboard;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.leaderboard.RankingRes;

import java.util.List;

public record DashboardData(
        List<RankingRes> leaderboardPreview,
        UserSummary userSummary,
        Statistics stats,
        GlobalStatistics globalStats,
        List<BetGameRes> upcomingGames,
        List<BetGameRes> recentResults
) {
}
