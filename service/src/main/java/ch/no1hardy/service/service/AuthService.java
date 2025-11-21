package ch.no1hardy.service.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    /**
     * Get the currently logged-in user.
     *
     * @return an Optional containing the logged-in user if authenticated, or empty if unauthenticated.
     */
    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticated()) return Optional.empty();

        User loggedInUser = (User) authentication.getPrincipal();
        return userRepository.findById(loggedInUser.getId());
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !authentication.getPrincipal().equals("anonymousUser");
    }
}
