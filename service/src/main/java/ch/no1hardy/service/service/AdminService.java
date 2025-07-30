package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
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

    public UserRes confirmUser(String id) {
        User user = userService.getRaw(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found", "Benutzer mit der ID " + id + " nicht gefunden"));
        if (user.isConfirmed()) return userMapper.toDto(user);

        user.confirm();
        notificationService.notifyUserApproval(user);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserRes denyUser(String id) {
        User user = userService.getRaw(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found", "Benutzer mit der ID " + id + " nicht gefunden"));
        if (user.getUserApplicationStatus() == UserApplicationStatus.DENIED) return userMapper.toDto(user);

        user.deny();
        notificationService.notifyUserRejection(user);
        return userMapper.toDto(userRepository.save(user));
    }
}
