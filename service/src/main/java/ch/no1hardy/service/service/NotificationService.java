package ch.no1hardy.service.service;

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

    public void notifyUsersNewGame(@NotNull Game game) {
        for (User user : userService.listRaw()) {
            createNewGameNotification(user, game);
        }

        globalDataService.updateGlobalData();
    }

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
     * @throws UsernameNotFoundException if a user somehow is in the leaderboard but not found in the database
     */
    public void notifyUsersRankingChange() throws UsernameNotFoundException {
        for (RankingRes res : leaderboardService.getLeaderboard()) {
            User user = userService.getRawByUsername(res.getUsername());
            int movement = res.getRanking() - res.getPrevRanking();
            createRankingNotification(user, movement);
        }

        globalDataService.updateGlobalData();
    }

    public void notifyUserApproval(@NotNull User user) {
        createApprovalNotification(user);
        globalDataService.updateGlobalData();
    }

    public void notifyUserRejection(@NotNull User user) {
        createRejectionNotification(user);
        globalDataService.updateGlobalData();
    }

    public void createNewGameNotification(@NotNull User user, @NotNull Game game) {
        String content = "Am " + game.getTimestamp().format(DateTimeFormatter.ofPattern("d.M.yy")) +
                " spielt " +
                game.getTeamHome().getName() +
                " gegen " +
                game.getTeamGuest().getName() +
                "." +
                " Gib jetzt deinen Tipp ab.";


        repository.save(Notification.builder()
                .type(NotificationType.NEW_BET)
                .title("Neues Spiel!")
                .content(content)
                .route("/bet/" + game.getGroup().getId() + "/" + game.getId())
                .user(user)
                .build());
    }

    public void createNewResultNotification(@NotNull User user, @NotNull Game game) {
        String content = "Das Resultat vom " + game.getTimestamp().format(DateTimeFormatter.ofPattern("d.M.yy")) +
                " - " +
                game.getTeamHome().getName() +
                " und " +
                game.getTeamGuest().getName() +
                " - ist nun verfügbar.";

        repository.save(Notification.builder()
                .type(NotificationType.NEW_RESULT)
                .title("Resultat verfügbar")
                .content(content)
                .route("/bet/" + game.getGroup().getId() + "/" + game.getId())
                .user(user)
                .build());
    }

    public void createRankingNotification(@NotNull User user, @NotNull int movement) {
        if (movement == 0) return;

        StringBuilder content = new StringBuilder();

        if (movement > 0) content.append("Gratuliere");
        else content.append("Schade");

        content.append(", du bist in der Rangliste um ").append(movement);

        if (Math.abs(movement) == 1) content.append(" Platz ");
        else content.append(" Plätze ");

        if (movement > 0) content.append("aufgestiegen");
        else content.append("abgestiegen");
        content.append(".");

        repository.save(Notification.builder()
                .type(NotificationType.NEW_RESULT)
                .title(movement > 0 ? "Aufstieg!" : "Abstieg!")
                .content(content.toString())
                .route("/leaderboard")
                .user(user)
                .build());
    }

    public void createApprovalNotification(@NotNull User user) {
        repository.save(Notification.builder()
                .type(NotificationType.APPROVAL)
                .title("Anfrage genehmigt")
                .content("Dein Account wurde freigeschaltet. Du bist nun auch in der Rangliste.")
                .route("/ranking")
                .user(user)
                .build());
    }

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
