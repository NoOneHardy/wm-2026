package ch.no1hardy.service.service;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.mapper.GameMapperImpl;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final GameMapperImpl mapper;
    private final UserService userService;

    public List<BetGameRes> listAll() {
        return repository.findAll().stream().map(this::mapUserBet).toList();
    }

    public BetGameRes create(GameReq dto) {
        Game game = repository.save(mapper.toEntity(dto));
        return mapUserBet(game);
    }

    public BetGameRes mapUserBet(Game game) {
        BetGameRes res = mapper.toDto(game);
        res.setBet(mapper.toDto(getBetFromUser(game)));
        return res;
    }

    public Bet getBetFromUser(Game game) {
        User user = this.userService.getLoggedInUser();
        if (user == null) return null;
        return game.getBets().stream()
                .filter(bet -> bet.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }
}
