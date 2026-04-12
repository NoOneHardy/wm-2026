package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.MissingRequestBodyException;
import ch.no1hardy.service.exception.MissingRequestPropertyException;
import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.mapper.TeamMapperImpl;
import ch.no1hardy.service.model.team.Team;
import ch.no1hardy.service.model.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository repository;
    private final TeamMapperImpl mapper;
    private final FileService fileService;

    public List<TeamRes> list() {
        return repository.findAll().stream()
                .filter(Team::isActive)
                .sorted(Comparator.comparing(Team::getName))
                .map(mapper::toDto)
                .toList();
    }

    public TeamRes create(TeamReq dto) {
        if (dto == null) throw new MissingRequestBodyException();

        if (dto.getName() == null) throw new MissingRequestPropertyException("name", "string");
        if (dto.getFlag() == null) throw new MissingRequestPropertyException("flag", "string");
        if (dto.getShortName() == null) throw new MissingRequestPropertyException("shortName", "string");

        Team team = repository.save(mapper.toEntity(dto));
        return mapper.toDto(team);
    }

    public String uploadFlag(MultipartFile flag) {
        return fileService.storeFlag(flag);
    }
}
