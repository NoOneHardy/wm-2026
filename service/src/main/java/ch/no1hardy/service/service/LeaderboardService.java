package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class LeaderboardService {
    private final UserService userService;
    private final AuthService authService;

    /**
     * Returns the leaderboard of all users, sorted by their current points.
     * The leaderboard contains the current ranking and the previous ranking.
     *
     * @return List of RankingRes containing user information and rankings
     */
    public List<RankingRes> getLeaderboard() {
        List<Integer> current = calculateLeaderboard(User::getPoints);
        List<Integer> previous = calculateLeaderboard(User::getLastReviewedPoints);

        return userService.listConfirmedRaw().stream()
                .map(user -> RankingRes.builder()
                        .id(user.getId())
                        .avatar(user.getAvatarUrl())
                        .ranking(current.indexOf(user.getPoints()) + 1)
                        .prevRanking(previous.indexOf(user.getLastReviewedPoints()) + 1)
                        .points(user.getPoints())
                        .username(user.getUsername())
                        .build()
                ).sorted(Comparator.comparingInt(RankingRes::getRanking)).toList();
    }

    /**
     * Returns the leaderboard of the current user, including their position and surrounding users.
     * The leaderboard contains the current ranking and the previous ranking.
     *
     * @return List of RankingRes containing user information and rankings
     * @throws NotLoggedInException  if the user is not logged in
     */
    public List<RankingRes> getUserLeaderboard() throws NotLoggedInException {
        return authService.getLoggedInUser()
                .map(this::getUserLeaderboard)
                .orElseThrow(NotLoggedInException::new);
    }

    /**
     * Returns the leaderboard of the specified user, including their position and surrounding users.
     * The leaderboard contains the current ranking and the previous ranking.
     *
     * @param user the user for whom to get the leaderboard
     * @return List of RankingRes containing user information and rankings
     */
    public List<RankingRes> getUserLeaderboard(@NotNull User user) {
        if (!user.isConfirmed()) return List.of();

        List<RankingRes> leaderboard = getLeaderboard();
        if (leaderboard.isEmpty()) return List.of();

        RankingRes[] slimBoard = new RankingRes[3];

        for (int i = 0; i < leaderboard.size(); i++) {
            RankingRes pos = leaderboard.get(i);
            if (pos.getId().equals(user.getId())) {
                slimBoard[0] = i == 0 ? null : leaderboard.get(i - 1);
                slimBoard[1] = pos;
                slimBoard[2] = i == (leaderboard.size() - 1) ? null : leaderboard.get(i + 1);
            }
        }

        return Arrays.stream(slimBoard).toList();
    }

    /**
     * Calculates the leaderboard based on the provided point accessor function.
     *
     * @param pointAccessorFn function to access points from a User
     * @return List of points sorted in descending order
     */
    private List<Integer> calculateLeaderboard(@NotNull Function<User, Integer> pointAccessorFn) {
        return userService.listConfirmedRaw().stream()
                .map(pointAccessorFn)
                .sorted(Integer::compareTo)
                .toList().reversed();
    }
}
