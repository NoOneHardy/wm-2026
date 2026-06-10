package ch.no1hardy.service.service;

import ch.no1hardy.service.common.DateHelper;
import ch.no1hardy.service.common.ListHelper;
import ch.no1hardy.service.exception.BetPlaceException;
import ch.no1hardy.service.exception.KnockoutTieException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ResultReq;
import ch.no1hardy.service.mapper.GameMapper;
import ch.no1hardy.service.mapper.UserHelper;
import ch.no1hardy.service.model.game.*;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final ScoreRepository scoreRepository;
    private final BetRepository betRepository;
    private final UserService userService;
    private final AuthService authService;
    private final GameMapper mapper;
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

    public BetGameRes uploadResult(String id, ResultReq dto) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) throw new NotFoundException("Game " + id + " not found", "Spiel '" + id + "' nicht gefunden");

        if (!dto.isValid()) return mapper.toDto(game);
        dto.clamp();

        removeExistingResult(game);
        if (dto.isNull()) return mapper.toDto(game);

        if (game.getGroup().getIsKnockout() && dto.isTie()) {
            throw new KnockoutTieException("Game " + id + " is in a knockout group and cannot end in a tie", "Spiel '" + id + "' ist in einer K.O.-Gruppe und kann nicht unentschieden enden");
        }

        dto.setGame(id);
        Score result = scoreRepository.save(mapper.toEntity(dto));
        game.setResult(result);

        userService.addUserPoints(game);
        notificationService.notifyUsersNewResult(game);
        notificationService.notifyUsersRankingChange();
        return mapper.toDto(repository.save(game));
    }

    private void removeExistingResult(@NotNull Game game) {
        Score existingResult = game.getResult();
        if (existingResult != null) {
            userService.removeUserPoints(game);

            game.setResult(null);
            scoreRepository.delete(existingResult);
        }
    }

    /**
     * Uploads a bet for a game.
     *
     * @param id  the ID of the game
     * @param dto the bet request containing the bet details
     * @return the updated game with the new bet
     * @throws BetPlaceException    if the game has already started or is in a knockout group with a tie
     * @throws NotFoundException    if the game with the given ID does not exist
     * @throws NotLoggedInException if the user is not logged in
     */
    public BetGameRes uploadBet(@NotNull String id, @NotNull BetReq dto) throws BetPlaceException, NotFoundException, NotLoggedInException {
        Game game = repository.findById(id).orElse(null);
        User user = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        if (game == null) throw new NotFoundException("Game " + id + " not found", "Spiel '" + id + "' nicht gefunden");

        if (game.getTimestamp().isBefore(DateHelper.getCurrentAbsoluteDate())) {
            throw new BetPlaceException("Game " + id + " has already started", "Dieses Spiel hat bereits begonnen");
        }

        if (game.getGroup().getIsKnockout() && dto.isTie()) {
            throw new KnockoutTieException("Game " + id + " is in a knockout group and cannot end in a tie", "Spiel '" + id + "' ist in einer K.O.-Gruppe und kann nicht unentschieden enden");
        }

        if (DateHelper.isBeforeNow(game.getTimestamp()) || game.getResult() != null || !dto.isValid()) {
            return mapper.toDto(game);
        }

        dto.clamp();
        removeExistingBet(game, user);
        if (dto.isNull()) return mapper.toDto(game);

        dto.setGame(id);
        dto.setUser(user.getId());
        Bet bet = betRepository.save(mapper.toEntity(dto));
        game.setBets(ListHelper.add(game.getBets(), bet));
        user.setBets(ListHelper.add(user.getBets(), bet));

        return mapper.toDto(repository.save(game));
    }

    private void removeExistingBet(@NotNull Game game, @NotNull User user) {
        Bet existingBet = userHelper.getUserBet(game.getBets());
        if (existingBet != null) {
            game.setBets(ListHelper.remove(game.getBets(), existingBet));
            user.setBets(ListHelper.remove(user.getBets(), existingBet));

            betRepository.delete(existingBet);
        }
    }

    /**
     * Returns a list of upcoming games that are still active and have not yet started.
     *
     * @return a list of upcoming games
     * @throws NotLoggedInException if the user is not logged in
     */
    public List<BetGameRes> getUpcomingGames() throws NotLoggedInException {
        return getAllActiveGames().stream()
                .filter(g -> !g.hasResult())
                .filter(g -> g.getTimestamp().isAfter(LocalDateTime.now()))
                .filter(g -> g.getTimestamp().isBefore(LocalDateTime.now().plusDays(5)))
                .sorted(Comparator.comparing(Game::getTimestamp))
                .limit(5)
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Returns the most recent results of games that are still active.
     *
     * @return a list of the most recent game results
     * @throws NotLoggedInException if the user is not logged in
     */
    public List<BetGameRes> getRecentResults() throws NotLoggedInException, UserNotFoundException {
        return getAllActiveGames().stream()
                .filter(Game::hasResult)
                .sorted((g1, g2) -> g2.getResult().getUpdatedAt().compareTo(g1.getResult().getUpdatedAt()))
                .limit(5)
                .map(mapper::toDto).toList();
    }

    public List<Game> getAllActiveGames() {
        return repository.findAll().stream()
                .filter(Game::isActive)
                .toList();
    }
}
