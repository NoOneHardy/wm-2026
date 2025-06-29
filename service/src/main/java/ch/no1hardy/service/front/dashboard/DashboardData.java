package ch.no1hardy.service.front.dashboard;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardData {
    private List<RankingRes> leaderboardPreview;
    private UserSummary userSummary;
    private Statistics stats;
    private GlobalStatistics globalStats;
    private List<BetGameRes> openBets;
}
