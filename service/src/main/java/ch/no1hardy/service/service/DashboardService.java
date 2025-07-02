package ch.no1hardy.service.service;

import ch.no1hardy.service.front.dashboard.DashboardData;
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
                .build();
    }
}
