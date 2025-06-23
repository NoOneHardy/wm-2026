package ch.no1hardy.service.front.dashboard;

import ch.no1hardy.service.front.leaderboard.RankingRes;
import lombok.Builder;

import java.util.List;

@Builder
public record DashboardData(
    List<RankingRes> leaderboardPreview
) {
}
