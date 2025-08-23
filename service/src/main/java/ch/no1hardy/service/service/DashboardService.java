package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.front.dashboard.GlobalStatistics;
import ch.no1hardy.service.front.dashboard.Statistics;
import ch.no1hardy.service.front.dashboard.UserSummary;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import jakarta.validation.constraints.NotNull;
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

    /**
     * Retrieves the dashboard data for the logged-in user.
     * @return a DashboardData object containing various statistics and summaries
     * @throws NotLoggedInException if the user is not logged in
     * @throws UserNotFoundException if the logged-in user is not found in the database
     */
    public DashboardData getDashboard() throws UserNotFoundException, NotLoggedInException {
        return DashboardData.builder()
                .leaderboardPreview(leaderboardService.getUserLeaderboard())
                .userSummary(getUserSummary())
                .upcomingGames(gameService.getUpcomingGames())
                .stats(getStatistics())
                .globalStats(getGlobalStatistics())
                .recentResults(gameService.getRecentResults())
                .build();
    }

    /**
     * Retrieves global statistics for all users.
     * @return a GlobalStatistics object containing total points, correct games, and wasted jokers
     */
    public GlobalStatistics getGlobalStatistics() {
        List<User> users = userService.listRaw();
        return GlobalStatistics.builder()
                .totalPoints(users.stream().mapToInt(User::getPoints).sum())
                .correctGames(users.stream().mapToInt(this::getCorrectGames).sum())
                .jokersWasted(users.stream().mapToInt(this::getJokersWasted).sum())
                .build();
    }

    /**
     * Retrieves a summary of the logged-in user's dashboard.
     * @return a UserSummary object containing the user's points, percentage of games bet, confirmation status, and ranking
     * @throws NotLoggedInException if the user is not logged in
     * @throws UserNotFoundException if the logged-in user is not found in the database
     * */
    public UserSummary getUserSummary() throws UserNotFoundException, NotLoggedInException {
        User user = userService.getCurrentUserRaw().orElseThrow(NotLoggedInException::new);

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
                .percentage(groupService.getOverallPercentage(user))
                .isConfirmed(isConfirmed)
                .ranking(userLeaderboard.size() == 3 ? userLeaderboard.get(1).getRanking() : null)
                .build();
    }

    /**
     * Calculates statistics for the logged-in user.
     * @return statistics for the logged-in user
     * @throws UserNotFoundException if the logged-in user is not found in the database
     * @throws NotLoggedInException if the user is not logged in
     */
    public Statistics getStatistics() throws UserNotFoundException, NotLoggedInException {
        User user = userService.getCurrentUserRaw().orElseThrow(NotLoggedInException::new);
        return getStatistics(user);
    }

    /**
     * Calculates statistics for a specific user.
     * @param user the user for whom to calculate statistics
     * @return statistics for the specified user
     */
    public Statistics getStatistics(@NotNull User user) {
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
    public int getTotalGoalsBet(@NotNull User user) {
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
    public int getCorrectGames(@NotNull User user) {
        return (int) user.getBets().stream()
                .filter(Bet::isActive)
                .filter(bet -> bet.getGame() != null && bet.getGame().getResult() != null)
                .filter(Bet::isCorrect)
                .count();
    }

    /**
     * Calculates the number of jokers wasted by the user.
     * A joker is considered wasted if it was used on a bet that did not yield any points.
     * @param user the user for whom to calculate wasted jokers
     * @return number of jokers wasted by the user
     */
    public int getJokersWasted(@NotNull User user) {
        return user.getBets().stream()
                .filter(Bet::isActive)
                .filter(bet -> bet.getGame() != null && bet.getGame().getResult() != null)
                .filter(bet -> bet.getJoker() > 1 && userService.calculateUserPoints(bet, bet.getGame().getResult()) <= 0)
                .mapToInt(bet -> bet.getJoker() - 1)
                .sum();
    }
}
