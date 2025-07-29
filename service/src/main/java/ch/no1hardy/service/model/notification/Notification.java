package ch.no1hardy.service.model.notification;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Notification extends BaseEntity {
    /**
     * The title of the notification.
     */
    private String title;

    /**
     * The content of the notification.
     */
    private String content;

    /**
     * The route to navigate to when the notification is clicked.
     */
    private String route;

    /**
     * The type of the notification
     */
    private NotificationType type;

    /**
     * Whether the notification has been read by the user.
     */
    private Boolean isRead = false;

    /**
     * The user who receives the notification.
     */
    @ManyToOne
    private User user;
}
