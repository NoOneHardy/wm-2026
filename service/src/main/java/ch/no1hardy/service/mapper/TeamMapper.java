package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.model.team.Team;
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
        uses = {TeamRepository.class}
)
public interface TeamMapper extends EntityMapper<Team, TeamReq, TeamRes> {
}
