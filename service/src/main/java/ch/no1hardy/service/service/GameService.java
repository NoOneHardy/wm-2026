package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.mapper.GameMapperImpl;
import ch.no1hardy.service.mapper.UserHelper;
import ch.no1hardy.service.model.game.*;
import ch.no1hardy.service.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final ScoreRepository scoreRepository;
    private final BetRepository betRepository;
    private final UserService userService;
    private final GameMapperImpl mapper;
    private final UserHelper userHelper;

    public List<BetGameRes> listAll() {
        return repository.findAll().stream().filter(Game::isActive).map(mapper::toDto).toList();
    }

    public BetGameRes create(GameReq dto) {
        Game game = repository.save(mapper.toEntity(dto));
        return mapper.toDto(game);
    }

    public BetGameRes uploadResult(String id, ScoreReq dto) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) throw new NotFoundException("Game " + id + " not found");

        dto.setGame(id);
        Score result;
        Score existingResult = game.getResult();
        if (existingResult != null) {
            if (existingResult.equals(dto)) return mapper.toDto(game);
            userService.removeUserPoints(game);
            mapper.update(dto, existingResult);
            result = existingResult;
        } else {
            result = scoreRepository.save(mapper.toEntity(dto));
        }
        game.setResult(result);

        userService.addUserPoints(game);
        return mapper.toDto(repository.save(game));
    }

    public BetGameRes uploadBet(String id, BetReq dto) {
        Game game = repository.findById(id).orElse(null);
        User user = userService.getLoggedInUser();
        if (game == null) throw new NotFoundException("Game " + id + " not found");
        if (user == null) throw new BadRequestException("User not logged in");

        if (game.getTimestamp().isBefore(LocalDateTime.now()) || game.getResult() != null)
            return mapper.toDto(game);

        dto.clampJoker();
        dto.setGame(id);
        dto.setUser(user.getId());
        Bet bet;
        Bet existingBet = userHelper.getUserBet(game.getBets());
        if (existingBet != null) {
            if (existingBet.equals(dto)) return mapper.toDto(game);
            mapper.update(dto, existingBet);
            bet = existingBet;
        } else {
            bet = betRepository.save(mapper.toEntity(dto));
        }
        List<Bet> oldBets = game.getBets().stream().filter(b -> !Objects.equals(b.getUser().getId(), bet.getUser().getId())).toList();
        List<Bet> bets = new ArrayList<>(oldBets);
        bets.add(bet);
        game.setBets(bets);

        List<Bet> oldUserBets = user.getBets().stream().filter(b -> !Objects.equals(b.getGame().getId(), bet.getGame().getId())).toList();
        List<Bet> userBets = new ArrayList<>(oldUserBets);
        userBets.add(bet);
        user.setBets(userBets);

        return mapper.toDto(repository.save(game));
    }
}
