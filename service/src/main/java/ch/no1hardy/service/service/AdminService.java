package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.mapper.UserMapperImpl;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import ch.no1hardy.service.model.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private UserService userService;
    private NotificationService notificationService;
    private UserMapperImpl userMapper;
    private UserRepository userRepository;

    /**
     * Confirms a user by their ID.
     * @param id the ID of the user to confirm
     * @return the confirmed user as a UserRes object
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public UserRes confirmUser(String id) throws UserNotFoundException {
        User user = userService.getRaw(id);
        if (user.isConfirmed()) return userMapper.toDto(user);

        user.confirm();
        notificationService.notifyUserApproval(user);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Denies a user by their ID.
     * @param id the ID of the user to deny
     * @return the denied user as a UserRes object
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public UserRes denyUser(String id) throws UserNotFoundException {
        User user = userService.getRaw(id);
        if (user.getUserApplicationStatus() == UserApplicationStatus.DENIED) return userMapper.toDto(user);

        user.deny();
        notificationService.notifyUserRejection(user);
        return userMapper.toDto(userRepository.save(user));
    }
}
