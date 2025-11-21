package ch.no1hardy.service.model.preferences;

import ch.no1hardy.service.model.notification.Channel;
import ch.no1hardy.service.model.notification.NotificationType;
import ch.no1hardy.service.model.user.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends ListCrudRepository<NotificationPreference, String> {
    Optional<NotificationPreference> findByUserAndChannelAndType(User user, Channel channel, NotificationType type);
}
