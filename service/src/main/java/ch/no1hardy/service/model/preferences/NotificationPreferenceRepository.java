package ch.no1hardy.service.model.preferences;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationPreferenceRepository extends ListCrudRepository<NotificationPreference, String> {
}
