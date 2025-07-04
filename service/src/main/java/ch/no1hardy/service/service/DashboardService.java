package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DashboardService {
    private final UserService userService;
    private final GameService gameService;

    public DashboardData getDashboard() {
        return DashboardData.builder()
                .leaderboardPreview(userService.getUserLeaderboard())
                .userSummary(userService.getUserSummary())
                .openBets(gameService.getOpenBets())
                .stats(userService.getStatistics())
                .globalStats(getGlobalStatistics())
                .build();
    }

    public GlobalStatistics getGlobalStatistics() {
        return GlobalStatistics.builder()
                .totalPoints(userService.listRaw().stream().mapToInt(User::getPoints).sum())
                .correctGames(userService.listRaw().stream().mapToInt(userService::getCorrectGames).sum())
                .jokersWasted(userService.listRaw().stream().mapToInt(userService::getJokersWasted).sum())
                .build();
    }
}
