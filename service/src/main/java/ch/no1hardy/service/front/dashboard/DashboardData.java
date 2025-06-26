package ch.no1hardy.service.front.dashboard;

import ch.no1hardy.service.front.leaderboard.RankingRes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardData {
    private List<RankingRes> leaderboardPreview;
}
