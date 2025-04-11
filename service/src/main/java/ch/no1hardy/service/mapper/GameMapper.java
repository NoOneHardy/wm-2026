package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetRes;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.team.TeamRepository;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {TeamRepository.class, GroupRepository.class, TeamMapper.class}
)
public interface GameMapper extends EntityMapper<Game, GameReq, BetGameRes> {
    BetRes toDto(Bet entity);

    ScoreRes toDto(Score entity);
}
