package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BetPlaceException;
import ch.no1hardy.service.exception.KnockoutTieException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.mapper.GameMapperImpl;
import ch.no1hardy.service.mapper.UserHelper;
import ch.no1hardy.service.model.game.*;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final NotificationService notificationService;

    public List<BetGameRes> list() {
        return repository.findAll().stream().filter(Game::isActive).map(mapper::toDto).toList();
    }

    public BetGameRes create(GameReq dto) {
        Game game = repository.save(mapper.toEntity(dto));
        notificationService.notifyUsersNewGame(game);
        return mapper.toDto(game);
    }

    public BetGameRes uploadResult(String id, ScoreReq dto) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) throw new NotFoundException("Game " + id + " not found", "Spiel '" + id + "' nicht gefunden");

        if (!dto.isValid()) return mapper.toDto(game);

        if (game.getGroup().getIsKnockout() && Objects.equals(dto.getScoreTeamGuest(), dto.getScoreTeamHome()) && dto.getScoreTeamHome() != null)
            throw new KnockoutTieException("Game " + id + " is in a knockout group and cannot end in a tie", "Spiel '" + id + "' ist in einer K.O.-Gruppe und kann nicht unentschieden enden");

        dto.setGame(id);
        Score result;
        Score existingResult = game.getResult();
        if (existingResult != null) {
            if (existingResult.equals(dto)) return mapper.toDto(game);

            if (dto.getScoreTeamGuest() == null && dto.getScoreTeamHome() == null) {
                userService.removeUserPoints(game);
                game.setResult(null);
                scoreRepository.delete(existingResult);
                return mapper.toDto(repository.save(game));
            }
            userService.removeUserPoints(game);
            mapper.update(dto, existingResult);
            result = existingResult;
        } else {
            result = scoreRepository.save(mapper.toEntity(dto));
        }
        game.setResult(result);

        userService.addUserPoints(game);
        notificationService.notifyUsersNewResult(game);
        notificationService.notifyUsersRankingChange();
        return mapper.toDto(repository.save(game));
    }

    public BetGameRes uploadBet(@NotNull String id, @NotNull BetReq dto) throws BetPlaceException, NotFoundException, NotLoggedInException {
        Game game = repository.findById(id).orElse(null);
        User user = userService.getCurrentUserRaw().orElseThrow(NotLoggedInException::new);
        if (game == null) throw new NotFoundException("Game " + id + " not found", "Spiel '" + id + "' nicht gefunden");

        if (game.getTimestamp().isBefore(LocalDateTime.now(ZoneId.of("CET")))) {
            throw new BetPlaceException("Game " + id + " has already started", "Dieses Spiel hat bereits begonnen");
        }

        if (game.getGroup().getIsKnockout() && Objects.equals(dto.getScoreTeamGuest(), dto.getScoreTeamHome()) && dto.getScoreTeamHome() != null)
            throw new KnockoutTieException("Game " + id + " is in a knockout group and cannot end in a tie", "Spiel '" + id + "' ist in einer K.O.-Gruppe und kann nicht unentschieden enden");

        if (game.getTimestamp().isBefore(LocalDateTime.now().atZone(ZoneId.of("CET")).toLocalDateTime())
                || game.getResult() != null
                || !dto.isValid())
            return mapper.toDto(game);

        dto.clampJoker();
        dto.setGame(id);
        dto.setUser(user.getId());
        Bet bet;
        Bet existingBet = userHelper.getUserBet(game.getBets());
        if (existingBet != null) {
            if (existingBet.equals(dto)) return mapper.toDto(game);

            if (dto.getScoreTeamGuest() == null && dto.getScoreTeamHome() == null) {
                betRepository.delete(existingBet);
                List<Bet> oldBets = game.getBets().stream().filter(b -> !Objects.equals(b.getUser().getId(), user.getId())).toList();
                List<Bet> bets = new ArrayList<>(oldBets);
                bets.remove(existingBet);
                game.setBets(bets);

                List<Bet> oldUserBets = user.getBets().stream().filter(b -> !Objects.equals(b.getGame().getId(), game.getId())).toList();
                List<Bet> userBets = new ArrayList<>(oldUserBets);
                userBets.remove(existingBet);
                user.setBets(userBets);

                return mapper.toDto(repository.save(game));
            }

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

    /**
     * Returns a list of upcoming games that are still active and have not yet started.
     * @return a list of upcoming games
     * @throws NotLoggedInException if the user is not logged in
     */
    public List<BetGameRes> getUpcomingGames() throws NotLoggedInException {
        return repository.findAll().stream()
                .filter(Game::isActive)
                .filter(g -> !g.hasResult())
                .filter(g -> g.getTimestamp().isAfter(LocalDateTime.now()))
                .filter(g -> g.getTimestamp().isBefore(LocalDateTime.now().plusDays(5)))
                .limit(5)
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Returns the most recent results of games that are still active.
     * @return a list of the most recent game results
     * @throws NotLoggedInException if the user is not logged in
     */
    public List<BetGameRes> getRecentResults() throws NotLoggedInException, UserNotFoundException {
        return repository.findAll().stream()
                .filter(Game::isActive)
                .filter(Game::hasResult)
                .sorted((g1, g2) -> g2.getResult().getUpdatedAt().compareTo(g1.getResult().getUpdatedAt()))
                .limit(5)
                .map(mapper::toDto).toList();
    }
}
