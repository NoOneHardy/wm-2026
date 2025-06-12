package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.front.group.GroupReq;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.mapper.GroupMapperImpl;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final GroupMapperImpl mapper;
    private final GameService gameService;

    public GroupRes create(GroupReq dto) {
        Group group = repository.save(mapper.toEntity(dto));
        return mapper.toDto(group);
    }

    public GroupRes getGroup(String id) {
        Group group = repository.findById(id).orElse(null);
        if (group == null) throw new NotFoundException("Group " + id + " not found");
        return mapper.toDto(group);
    }

    public GroupRes updateBets(String id, List<BetReq> bets) {
        Group group = repository.findById(id).orElse(null);
        if (group == null) throw new NotFoundException("Group " + id + " not found");

        for (BetReq bet : bets.stream().filter(BetReq::isValid).toList()) {
            if (group.getGames().stream().filter(Game::isActive).map(Game::getId).toList().contains(bet.getGame())) {
                gameService.uploadBet(bet.getGame(), bet);
            }
        }
        return mapper.toDto(group);
    }

    public GroupRes updateResults(String id, List<ScoreReq> results) {
        Group group = repository.findById(id).orElse(null);
        if (group == null) throw new NotFoundException("Group " + id + " not found");

        for (ScoreReq result : results.stream().filter(ScoreReq::isValid).toList()) {
            if (group.getGames().stream().filter(Game::isActive).map(Game::getId).toList().contains(result.getGame())) {
                gameService.uploadResult(result.getGame(), result);
            }
        }
        return mapper.toDto(group);
    }

    public List<CardGroupRes> getCardGroups() {
        return repository.findAll().stream()
                .filter(Group::isActive)
                .filter(group -> !group.getGames().isEmpty())
                .map(group -> mapper.toCard(group, repository))
                .toList();
    }
}
