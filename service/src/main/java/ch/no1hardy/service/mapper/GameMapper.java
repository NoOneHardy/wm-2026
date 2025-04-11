package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.game.*;
import ch.no1hardy.service.model.game.*;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.team.TeamRepository;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {TeamRepository.class, GroupRepository.class, TeamMapper.class, UserHelper.class, GameRepository.class}
)
public interface GameMapper extends EntityMapper<Game, GameReq, BetGameRes> {
    BetRes toDto(Bet entity);

    @Mapping(target = "bet", source = "bets", qualifiedByName = "getUserBet")
    BetGameRes toDto(Game game);

    @Mapping(target = "gameId", source = "game", qualifiedByName = "getGameId")
    ScoreRes toDto(Score entity);

    void update(ScoreReq dto, @MappingTarget Score entity);

    Score toEntity(ScoreReq dto);

    @Named("getGameId")
    default String getId(Game game) {
        return game.getId();
    }
}