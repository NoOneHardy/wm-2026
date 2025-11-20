package ch.no1hardy.service.model.preferences;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class NotificationPreference extends BaseEntity {
    @ManyToOne
    private User user;
    private NotificationType type;
    private boolean selected;
}
