package ch.no1hardy.service.model.user;

import ch.no1hardy.service.model.notification.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deletedAt IS NULL")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    Optional<User> findByEmail(String email);

    User getUserById(String id);

    @Query("SELECT n FROM Notification n WHERE n.user = :user")
    List<Notification> listNotifications(@Param("user") User user);
}
