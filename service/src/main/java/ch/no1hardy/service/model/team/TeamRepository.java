package ch.no1hardy.service.model.team;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends ListCrudRepository<Team, String> {
    Team getTeamById(String id);
}
