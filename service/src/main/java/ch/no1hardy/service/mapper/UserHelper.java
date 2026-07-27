package ch.no1hardy.service.mapper;

import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.AuthService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserHelper {
    @Value("${beticon.joker.double}")
    private Integer doubleJokerLimit;
    @Value("${beticon.joker.triple}")
    private Integer tripleJokerLimit;

    private final AuthService authService;

    @Named("getDoubleJokerLimit")
    public Integer getDoubleJokerLimit() {
        return doubleJokerLimit;
    }

    @Named("getTripleJokerLimit")
    public Integer getTripleJokerLimit() {
        return tripleJokerLimit;
    }

    /**
     * Returns the user's bet from a list of bets.
     * @param bets the list of bets to search through
     * @return the user's bet if found
     * @throws NotLoggedInException if the user is not logged in
     */
    @Named("getUserBet")
    public Bet getUserBet(@NotNull List<Bet> bets) throws NotLoggedInException {
        User user = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);

        return bets.stream()
                .filter(bet -> bet.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the number of available double jokers for the current user.
     * @return the number of available double jokers
     */
    @Named("getAvailableDoubleJokers")
    public int getAvailableDoubleJokers() {
        Optional<User> user$ = authService.getLoggedInUser();
        if (user$.isEmpty()) return getDoubleJokerLimit();

        User user = user$.get();
        int usedDoubleJokers = user.getBets().stream().filter(b -> b.getJoker() == 2).toList().size();
        return getDoubleJokerLimit() - usedDoubleJokers;
    }

    @Named("getAvailableTripleJokers")
    public int getAvailableTripleJokers() {
        Optional<User> user$ = authService.getLoggedInUser();
        if (user$.isEmpty()) return getTripleJokerLimit();

        User user = user$.get();
        int usedTripleJokers = user.getBets().stream().filter(b -> b.getJoker() == 3).toList().size();
        return getTripleJokerLimit() - usedTripleJokers;
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
