package ch.no1hardy.service.mapper;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class UserHelper {
    private UserService service;

    @Named("getUserBet")
    public Bet getUserBet(List<Bet> bets) {
        User user = this.service.getLoggedInUser();
        if (user == null) return null;
        return bets.stream()
                .filter(bet -> bet.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    @Named("getAvailableDoubleJokers")
    public Integer getAvailableDoubleJokers(Integer maxDoubleJokers) {
        User user = this.service.getLoggedInUser();
        if (user == null) return maxDoubleJokers;

        Integer usedDoubleJokers = user.getBets().stream().filter(b -> b.getJoker() == 2).toList().size();

        return maxDoubleJokers - usedDoubleJokers;
    }

    @Named("getPercentage")
    public Double getPercentage(List<Game> games) {
        double betCount = games.stream().map(g -> getUserBet(g.getBets())).filter(Objects::nonNull).toList().size();
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
