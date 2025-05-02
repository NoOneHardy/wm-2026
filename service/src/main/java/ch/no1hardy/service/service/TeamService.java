package ch.no1hardy.service.service;

import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.mapper.TeamMapperImpl;
import ch.no1hardy.service.model.team.Team;
import ch.no1hardy.service.model.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository repository;
    private final TeamMapperImpl mapper;

    public TeamRes create(TeamReq dto) {
        Team team = repository.save(mapper.toEntity(dto));
        return mapper.toDto(team);
    }
}
