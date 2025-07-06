package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .recentResults(gameService.getRecentResults())
                .build();
    }

    public GlobalStatistics getGlobalStatistics() {
        List<User> users = userService.listRaw();
        return GlobalStatistics.builder()
                .totalPoints(users.stream().mapToInt(User::getPoints).sum())
                .correctGames(users.stream().mapToInt(userService::getCorrectGames).sum())
                .jokersWasted(users.stream().mapToInt(userService::getJokersWasted).sum())
                .build();
    }
}
