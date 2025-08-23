package ch.no1hardy.service.mapper;

import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserHelper {
    private UserService service;

    /**
     * Returns the user's bet from a list of bets.
     * @param bets the list of bets to search through
     * @return the user's bet if found
     * @throws UserNotFoundException if the logged-in user is not found in the database
     * @throws NotLoggedInException if the user is not logged in
     */
    @Named("getUserBet")
    public Bet getUserBet(@NotNull List<Bet> bets) throws UserNotFoundException, NotLoggedInException {
        User user = this.service.getCurrentUserRaw().orElseThrow(NotLoggedInException::new);

        return bets.stream()
                .filter(bet -> bet.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the number of available double jokers for the current user.
     * @param maxDoubleJokers the maximum number of double jokers a user can have
     * @return the number of available double jokers
     * @throws UserNotFoundException if the logged-in user is not found in the database
     */
    @Named("getAvailableDoubleJokers")
    public int getAvailableDoubleJokers(int maxDoubleJokers) throws UserNotFoundException {
        Optional<User> user$ = this.service.getCurrentUserRaw();
        if (user$.isEmpty()) return maxDoubleJokers;

        User user = user$.get();
        int usedDoubleJokers = user.getBets().stream().filter(b -> b.getJoker() == 2).toList().size();
        return maxDoubleJokers - usedDoubleJokers;
    }

    @Named("getAvailableTripleJokers")
    public int getAvailableTripleJokers(int maxTripleJokers) throws UserNotFoundException {
        Optional<User> user$ = this.service.getCurrentUserRaw();
        if (user$.isEmpty()) return maxTripleJokers;

        User user = user$.get();
        int usedTripleJokers = user.getBets().stream().filter(b -> b.getJoker() == 3).toList().size();
        return maxTripleJokers - usedTripleJokers;
    }

    @Named("getPercentage")
    public Double getPercentage(List<Game> games) {
        double betCount = games.stream()
                .filter(g -> getUserBet(g.getBets()) != null)
                .toList().size();
        double gameCount = games.size();
        return gameCount == 0 ? 0.0 : betCount / gameCount * 100;
    }

    @Named("getLastSavedAt")
    public LocalDateTime getLastSaved(List<Game> games) {
        List<Bet> bets = games.stream().map(g -> getUserBet(g.getBets())).filter(Objects::nonNull).toList();
        if (bets.isEmpty()) return null;
        return bets.stream()
                .map(Bet::getUpdatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
}
