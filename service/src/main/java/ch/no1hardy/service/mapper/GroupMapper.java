package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.group.GroupReq;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import org.mapstruct.*;

import java.time.LocalDateTime;
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
    GroupRes toDto(Group entity);

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
}
