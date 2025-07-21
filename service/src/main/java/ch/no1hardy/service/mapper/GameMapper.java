package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.game.*;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.team.TeamRepository;
import ch.no1hardy.service.model.user.UserRepository;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {TeamRepository.class, GroupRepository.class, TeamMapper.class, UserHelper.class, GameRepository.class, UserRepository.class}
)
public interface GameMapper extends EntityMapper<Game, GameReq, BetGameRes> {
    @Mapping(target = "gameId", source = "game", qualifiedByName = "getGameId")
    BetRes toDto(Bet entity);

    @Mapping(target = "bet", source = "bets", qualifiedByName = "getUserBet")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    BetGameRes toDto(Game game);

    @Mapping(target = "gameId", source = "game", qualifiedByName = "getGameId")
    ScoreRes toDto(Score entity);

    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, target = "scoreTeamHome")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, target = "scoreTeamGuest")
    void update(ScoreReq dto, @MappingTarget Score entity);

    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, target = "scoreTeamHome")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, target = "scoreTeamGuest")
    void update(BetReq dto, @MappingTarget Bet entity);

    Score toEntity(ScoreReq dto);

    Bet toEntity(BetReq dto);

    @Named("getGameId")
    default String getId(Game game) {
        return game.getId();
    }
}