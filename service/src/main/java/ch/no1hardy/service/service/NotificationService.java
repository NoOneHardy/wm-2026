package ch.no1hardy.service.service;

import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.notification.NotificationRepository;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final GlobalDataService globalDataService;
    private final NotificationRepository repository;

    public void notifyUsersNewGame(Game game) {
        for (User user : userService.listRaw()) {
            createNewGameNotification(user, game);
        }

        globalDataService.updateGlobalData();
    }

    public void createNewGameNotification(User user, Game game) {
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
}
