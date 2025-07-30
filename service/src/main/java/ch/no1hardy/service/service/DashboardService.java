package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {
    private final UserService userService;
    private final GameService gameService;
    private final LeaderboardService leaderboardService;
    private final GroupService groupService;

    public DashboardData getDashboard() {
        return DashboardData.builder()
                .leaderboardPreview(leaderboardService.getUserLeaderboard())
                .userSummary(getUserSummary())
                .upcomingGames(gameService.getUpcomingGames())
                .stats(getStatistics())
                .globalStats(getGlobalStatistics())
                .recentResults(gameService.getRecentResults())
                .build();
    }

    public GlobalStatistics getGlobalStatistics() {
        List<User> users = userService.listRaw();
        return GlobalStatistics.builder()
                .totalPoints(users.stream().mapToInt(User::getPoints).sum())
                .correctGames(users.stream().mapToInt(this::getCorrectGames).sum())
                .jokersWasted(users.stream().mapToInt(this::getJokersWasted).sum())
                .build();
    }

    public UserSummary getUserSummary() {
        User user = userService.getLoggedInUser();
        if (user == null) throw new BadRequestException("Not logged in", "Benutzer nicht angemeldet");

        Boolean isConfirmed;
        if (user.getUserApplicationStatus() == UserApplicationStatus.ACCEPTED && user.getApplicationReviewedAt() != null)
            isConfirmed = true;
        else if (user.getUserApplicationStatus() == UserApplicationStatus.DENIED && user.getApplicationReviewedAt() != null)
            isConfirmed = false;
        else
            isConfirmed = null;

        List<RankingRes> userLeaderboard = leaderboardService.getUserLeaderboard(user);
        return UserSummary.builder()
                .points(user.getPoints())
                .percentage(getOverallPercentage(user))
                .isConfirmed(isConfirmed)
                .ranking(userLeaderboard.size() == 3 ? userLeaderboard.get(1).getRanking() : null)
                .build();
    }

    public Double getOverallPercentage(User user) {
        int totalBets = user.getBets().stream().filter(Bet::isActive).toList().size();

        int games = groupService.listRaw().stream()
                .mapToInt((group) -> group.getGames().stream().filter(Game::isActive).toList().size()).sum();
        if (games == 0) return 0.0;
        return (double) (totalBets * 100) / games;
    }

    /**
     * Calculates statistics for the logged-in user.
     * @return statistics for the logged-in user
     */
    public Statistics getStatistics() {
        User user = userService.getLoggedInUser();
        if (user == null) throw new BadRequestException("Not logged in", "Benutzer nicht angemeldet");

        return getStatistics(user);
    }

    /**
     * Calculates statistics for a specific user.
     * @param user the user for whom to calculate statistics
     * @return statistics for the specified user
     */
    public Statistics getStatistics(User user) {
        return Statistics.builder()
                .totalGoalsBet(getTotalGoalsBet(user))
                .correctGames(getCorrectGames(user))
                .jokersWasted(getJokersWasted(user))
                .build();
    }

    /**
     * Calculates the total number of goals bet by the user.
     * @param user the user for whom to calculate total goals bet
     * @return total number of goals bet by the user
     */
    public int getTotalGoalsBet(User user) {
        return user.getBets().stream()
                .filter(Bet::isActive)
                .mapToInt(bet -> bet.getScoreTeamHome() + bet.getScoreTeamGuest())
                .sum();
    }

    /**
     * Calculates the number of games correctly predicted by the user.
     * @param user the user for whom to calculate correct games
     * @return number of games correctly predicted by the user
     */
    public int getCorrectGames(User user) {
        return (int) user.getBets().stream()
                .filter(Bet::isActive)
                .filter(bet -> bet.getGame() != null && bet.getGame().getResult() != null)
                .filter(bet -> bet.getScoreTeamHome().equals(bet.getGame().getResult().getScoreTeamHome()) &&
                        bet.getScoreTeamGuest().equals(bet.getGame().getResult().getScoreTeamGuest()))
                .count();
    }

    /**
     * Calculates the number of jokers wasted by the user.
     * A joker is considered wasted if it was used on a bet that did not yield any points.
     * @param user the user for whom to calculate wasted jokers
     * @return number of jokers wasted by the user
     */
    public int getJokersWasted(User user) {
        return user.getBets().stream()
                .filter(Bet::isActive)
                .filter(bet -> bet.getGame() != null && bet.getGame().getResult() != null)
                .filter(bet -> bet.getJoker() > 1 && userService.calculateUserPoints(bet, bet.getGame().getResult()) <= 0)
                .mapToInt(bet -> bet.getJoker() - 1)
                .sum();
    }
}
