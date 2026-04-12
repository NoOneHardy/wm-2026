package ch.no1hardy.service.model.game;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends ListCrudRepository<Game, String> {
    Game getById(String id);
}
