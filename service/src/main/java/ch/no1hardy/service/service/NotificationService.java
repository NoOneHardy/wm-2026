package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.notification.NotificationNotFoundException;
import ch.no1hardy.service.exception.user.UsernameNotFoundException;
import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.notification.NotificationRepository;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final GlobalDataService globalDataService;
    private final NotificationRepository repository;
    private final LeaderboardService leaderboardService;

    /**
     * Fetches a notification by its ID.
     *
     * @param id the ID of the notification to fetch
     * @return the Notification object if found
     * @throws NotificationNotFoundException if no notification with the given ID exists
     */
    public Notification getRaw(@NotNull String id) {
        return repository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    }

    /**
     * Marks a notification as read by setting its 'isRead' property to true.
     *
     * @param id the ID of the notification to mark as read
     * @throws NotificationNotFoundException if no notification with the given ID exists
     */
    public void markNotificationAsRead(@NotNull String id) throws NotificationNotFoundException {
        Notification notification = getRaw(id);
        notification.setIsRead(true);
        globalDataService.updateGlobalData();
        repository.save(notification);
    }

    /**
     * Notifies all users about a new game.
     * This method iterates through all users and creates a notification for each user
     * with the details of the new game.
     *
     * @param game the game for which the notification is created
     */
    public void notifyUsersNewGame(@NotNull Game game) {
        for (User user : userService.listRaw()) {
            createNewGameNotification(user, game);
        }

        globalDataService.updateGlobalData();
    }

    /**
     * Notifies all users about a new game result.
     * This method iterates through all users and creates a notification for each user
     * with the details of the new game result.
     *
     * @param game the game for which the result is available
     */
    public void notifyUsersNewResult(@NotNull Game game) {
        for (User user : userService.listRaw()) {
            createNewResultNotification(user, game);
        }

        globalDataService.updateGlobalData();
    }

    /**
     * Notifies all users about their ranking change.
     * This method iterates through the leaderboard and creates a notification for each user
     * based on their ranking movement.
     *
     * @throws UsernameNotFoundException if a user somehow is in the leaderboard but not found in the database
     */
    public void notifyUsersRankingChange() {
        for (RankingRes res : leaderboardService.getLeaderboard()) {
            User user = userService.getRawByUsername(res.username());
            int movement = res.prevRanking() - res.ranking();
            createRankingNotification(user, movement);
        }

        globalDataService.updateGlobalData();
    }

    /**
     * Notifies a user that their account has been approved.
     * This method creates a notification for the user indicating that their account has been approved
     * and updates the global data.
     *
     * @param user the user to notify
     */
    public void notifyUserApproval(@NotNull User user) {
        createApprovalNotification(user);
        globalDataService.updateGlobalData();
    }

    /**
     * Notifies a user that their account has been rejected.
     * This method creates a notification for the user indicating that their account has been rejected
     * and updates the global data.
     *
     * @param user the user to notify
     */
    public void notifyUserRejection(@NotNull User user) {
        createRejectionNotification(user);
        globalDataService.updateGlobalData();
    }

    /**
     * Creates a new game notification for a user.
     * This method constructs a notification with the game details and saves it to the repository.
     *
     * @param user the user to notify
     * @param game the game for which the notification is created
     */
    public void createNewGameNotification(@NotNull User user, @NotNull Game game) {
        String content = String.format(
                "Am %s spielt %s gegen %s. Gib jetzt deinen Tipp ab.",
                game.getTimestamp().format(DateTimeFormatter.ofPattern("d.M.yy")),
                game.getTeamGuest().getName(),
                game.getTeamHome().getName()
        );

        repository.save(Notification.builder()
                .type(NotificationType.NEW_BET)
                .title("Neues Spiel!")
                .content(content)
                .route("/bets/" + game.getGroup().getId() + "/" + game.getId())
                .user(user)
                .build());
    }

    /**
     * Creates a new result notification for a user.
     * This method constructs a notification with the game result details and saves it to the repository.
     *
     * @param user the user to notify
     * @param game the game for which the result is available
     */
    public void createNewResultNotification(@NotNull User user, @NotNull Game game) {
        String content = String.format(
                "Das Resultat vom %s - %s gegen %s - ist nun verfügbar.",
                game.getTimestamp().format(DateTimeFormatter.ofPattern("d.M.yy")),
                game.getTeamHome().getName(),
                game.getTeamGuest().getName()
        );

        repository.save(Notification.builder()
                .type(NotificationType.NEW_RESULT)
                .title("Resultat verfügbar")
                .content(content)
                .route("/bets/" + game.getGroup().getId() + "/" + game.getId())
                .user(user)
                .build());
    }

    /**
     * Creates a ranking notification for a user based on their movement in the leaderboard.
     * This method constructs a notification indicating whether the user has moved up or down in the rankings
     * and saves it to the repository.
     *
     * @param user     the user to notify
     * @param movement the change in ranking position (positive for upward movement, negative for downward)
     */
    public void createRankingNotification(@NotNull User user, int movement) {
        if (movement == 0) return;

        StringBuilder content = new StringBuilder();
        if (movement > 0) content.append("Gratuliere");
        else content.append("Schade");

        content.append(", du bist in der Rangliste um ").append(Math.abs(movement));
        if (Math.abs(movement) == 1) content.append(" Platz ");
        else content.append(" Plätze ");

        if (movement > 0) content.append("aufgestiegen");
        else content.append("abgestiegen");
        content.append(".");

        repository.save(Notification.builder()
                .type(NotificationType.RANKING_UPDATE)
                .title(movement > 0 ? "Aufstieg!" : "Abstieg!")
                .content(content.toString())
                .route("/leaderboard")
                .user(user)
                .build());
    }

    /**
     * Creates an approval notification for a user.
     * This method constructs a notification indicating that the user's account has been approved
     * and saves it to the repository.
     *
     * @param user the user to notify
     */
    public void createApprovalNotification(@NotNull User user) {
        repository.save(Notification.builder()
                .type(NotificationType.APPROVAL)
                .title("Anfrage genehmigt")
                .content("Dein Account wurde freigeschaltet. Du bist nun auch in der Rangliste.")
                .route("/ranking")
                .user(user)
                .build());
    }

    /**
     * Creates a rejection notification for a user.
     * This method constructs a notification indicating that the user's account has been rejected
     * and saves it to the repository.
     *
     * @param user the user to notify
     */
    public void createRejectionNotification(@NotNull User user) {
        repository.save(Notification.builder()
                .type(NotificationType.REJECTION)
                .title("Anfrage abgelehnt")
                .content("Dein Account wurde abgelehnt. Sollte dies ein Fehler sein, kontaktiere bitte einen Admin oder lies dir die Teilnahmebedingungen erneut durch.")
                .route("/ranking")
                .user(user)
                .build());
    }
}
