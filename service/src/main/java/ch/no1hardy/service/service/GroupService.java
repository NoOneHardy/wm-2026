package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BetPlaceException;
import ch.no1hardy.service.exception.KnockoutTieException;
import ch.no1hardy.service.exception.MissingRequestBodyException;
import ch.no1hardy.service.exception.MissingRequestPropertyException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.ResultReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.front.group.GroupOptionRes;
import ch.no1hardy.service.front.group.GroupReq;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.front.group.OverviewRes;
import ch.no1hardy.service.mapper.GroupMapperImpl;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final GroupMapperImpl mapper;
    private final GameService gameService;
    private final AuthService authService;

    public GroupRes create(GroupReq dto) {
        if (dto == null) throw new MissingRequestBodyException();
        if (dto.getName() == null) throw new MissingRequestPropertyException("name", "string");
        if (dto.getIsKnockout() == null) throw new MissingRequestPropertyException("isKnockout", "boolean");
        if (dto.getOrder() == null) throw new MissingRequestPropertyException("order", "number");

        Group group = repository.save(mapper.toEntity(dto));
        return mapper.toDto(group);
    }

    /**
     * Returns a list of all active groups.
     *
     * @return List of active groups
     */
    public List<Group> listRaw() {
        return repository.findAll().stream()
                .filter(Group::isActive)
                .toList();
    }

    public List<GroupOptionRes> listOptions() {
        return listRaw().stream()
                .sorted(Comparator.comparing(Group::getOrder).thenComparing(Group::getName))
                .map(group -> new GroupOptionRes(
                        group.getId(),
                        group.getName(),
                        group.getIsKnockout(),
                        group.getOrder(),
                        group.getThumbnail()
                ))
                .toList();
    }

    public GroupRes getGroup(String id) {
        Group group = repository.findById(id).orElse(null);
        if (group == null)
            throw new NotFoundException("Group " + id + " not found", "Gruppe '" + id + "' nicht gefunden");
        return mapper.toDto(group);
    }

    public GroupRes updateBets(String id, List<BetReq> bets) {
        Group group = repository.findById(id).orElse(null);
        if (group == null)
            throw new NotFoundException("Group " + id + " not found", "Gruppe '" + id + "' nicht gefunden");

        for (BetReq bet : bets.stream().filter(BetReq::isValid).toList()) {
            if (group.getGames().stream().filter(Game::isActive).map(Game::getId).toList().contains(bet.getGame())) {
                try {
                    gameService.uploadBet(bet.getGame(), bet);
                } catch (BetPlaceException e) {
                    throw new BetPlaceException(e.getMessage(), "Eines der Spiele hat bereits begonnen.");
                } catch (KnockoutTieException e) {
                    throw new KnockoutTieException(e.getMessage(), "Diese Gruppe ist eine K.O.-Phase. Spiele können nicht unentschieden enden.");
                }
            }
        }
        return mapper.toDto(group);
    }

    public GroupRes updateResults(String id, List<ResultReq> results) {
        Group group = repository.findById(id).orElseThrow(() -> new NotFoundException("Group " + id + " not found", "Gruppe '" + id + "' nicht gefunden"));

        for (ResultReq result : results.stream().filter(ScoreReq::isValid).toList()) {
            Optional<Game> gameOptional = group.getGames().stream().filter(g -> g.isActive() && g.getId().equals(result.getGame())).findFirst();
            if (gameOptional.isEmpty()) continue;

            Game game = gameOptional.get();
            if (game.getResult() != null && game.getResult().isActive()) {
                if (Objects.equals(game.getResult().getScoreTeamGuest(), result.getScoreTeamGuest())
                        && Objects.equals(game.getResult().getScoreTeamHome(), result.getScoreTeamHome())) continue;
            }

            try {
                gameService.uploadResult(result.getGame(), result);
            } catch (KnockoutTieException e) {
                throw new KnockoutTieException(e.getMessage(), "Diese Gruppe ist eine K.O.-Phase. Spiele können nicht unentschieden enden.");
            }
        }
        return mapper.toDto(group);
    }

    /**
     * Calculates the overall percentage of games bet by the logged-in user.
     *
     * @return the overall percentage of games bet by the logged-in user
     */
    public Double getOverallPercentage() throws NotLoggedInException {
        User user = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        return getOverallPercentage(user);
    }

    /**
     * Calculates the overall percentage of games bet by the user.
     *
     * @param user the user for whom to calculate the overall percentage
     * @return the overall percentage of games bet by the user
     */
    public Double getOverallPercentage(@NotNull User user) {
        int totalBets = user.getBets().stream().filter(Bet::isActive).toList().size();

        int games = listRaw().stream()
                .mapToInt((group) -> group.getGames().stream().filter(Game::isActive).toList().size()).sum();
        if (games == 0) return 0.0;
        return (double) (totalBets * 100) / games;
    }

    public Double getOverallResultPercentage() {
        long totalResults = listRaw().stream()
                .map(Group::getGames)
                .flatMap(List::stream)
                .filter(Game::isActive)
                .filter(Game::hasResult)
                .count();
        long games = listRaw().stream()
                .map(Group::getGames)
                .flatMap(List::stream)
                .filter(Game::isActive)
                .count();

        if (games == 0) return 0.0;
        return (double) (totalResults * 100) / games;
    }

    public List<CardGroupRes> getCardGroups() {
        return listRaw().stream()
                .filter(group -> !group.getGames().isEmpty())
                .map(group -> mapper.toCard(group, repository))
                .toList();
    }

    public OverviewRes getOverview() {
        return new OverviewRes(
                this.getOverallPercentage(),
                this.getOverallResultPercentage(),
                this.getCardGroups()
        );
    }
}
