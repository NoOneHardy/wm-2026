package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetReq;
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
        return mapFromEntity(group);
    }

    public GroupRes getGroup(String id) {
        Group group = repository.findById(id).orElse(null);
        if (group == null) throw new NotFoundException("Group " + id + " not found");
        return mapFromEntity(group);
    }

    public GroupRes updateBets(String id, List<BetReq> bets) {
        Group group = repository.findById(id).orElse(null);
        if (group == null) throw new NotFoundException("Group " + id + " not found");

        List<Game> games = group.getGames();
        if (games.isEmpty()) throw new NotFoundException("Group " + id + " has no games");

        for (BetReq bet : bets) {
            if (games.stream().map(Game::getId).toList().contains(bet.getGame())) {
                gameService.uploadBet(bet.getGame(), bet);
            }
        }
        return mapFromEntity(group);
    }


    // TODO: remove
    private static GroupRes mapFromEntity(Group group) {
        return GroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .games(group.getGames().stream().map(g -> BetGameRes.builder().build()).toList())
                .isKnockout(group.getIsKnockout())
                .percentage(10.0)
                .lastSavedAt(null)
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .deletedAt(group.getDeletedAt())
                .build();
    }
}
