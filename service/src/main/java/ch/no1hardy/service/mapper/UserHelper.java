package ch.no1hardy.service.mapper;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
