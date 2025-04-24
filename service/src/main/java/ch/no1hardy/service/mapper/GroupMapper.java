package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.front.group.GroupReq;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.team.Team;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {GameMapper.class, UserHelper.class}
)
public interface GroupMapper extends EntityMapper<Group, GroupReq, GroupRes> {
    @Mapping(target = "percentage", source = "games", qualifiedByName = "getPercentage")
    @Mapping(target = "lastSavedAt", source = "games", qualifiedByName = "getLastSavedAt")
    @Mapping(target = "percentageResult", source = "games", qualifiedByName = "getPercentageResult")
    @Mapping(target = "lastSavedAtResult", source = "games", qualifiedByName = "getLastSavedAtResult")
    @Mapping(target = "availableDoubleJokers", constant = "8", qualifiedByName = "getAvailableDoubleJokers")
    GroupRes toDto(Group entity);

    @Mapping(target = "percentage", source = "games", qualifiedByName = "getPercentage")
    @Mapping(target = "percentageResult", source = "games", qualifiedByName = "getPercentageResult")
    @Mapping(target = "thumbnail", source = "id", qualifiedByName = "getThumbnail")
    CardGroupRes toCard(Group entity, @Context GroupRepository groupRepository);

    @Named("getPercentageResult")
    default Double getPercentageResult(List<Game> games) {
        double betCount = games.stream().map(Game::getResult).filter(Objects::nonNull).toList().size();
        double gameCount = games.size();
        return gameCount == 0 ? 0.0 : betCount / gameCount * 100;
    }

    @Named("getLastSavedAtResult")
    default LocalDateTime getLastSavedAtResult(List<Game> games) {
        List<Score> scores = games.stream().map(Game::getResult).filter(Objects::nonNull).toList();
        if (scores.isEmpty()) return null;
        return scores.stream()
                .map(Score::getUpdatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Named("getThumbnail")
    default List<String> getThumbnail(String id, @Context GroupRepository groupRepository) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) return List.of();

        if (group.getIsKnockout() && group.getThumbnail() != null) {
            return List.of(group.getThumbnail());
        }

        List<Game> games = group.getGames();
        if (games.isEmpty()) return List.of();

        List<Team> teams = new ArrayList<>();
        for (Game game : games) {
            if (game.getTeamHome() != null && !teams.contains(game.getTeamHome())) {
                teams.add(game.getTeamHome());
            }
            if (game.getTeamGuest() != null && !teams.contains(game.getTeamGuest())) {
                teams.add(game.getTeamGuest());
            }
        }
        return teams.stream().map(Team::getFlag).toList();
    }
}
