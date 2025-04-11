package ch.no1hardy.service.model.group;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends ListCrudRepository<Group, String> {
    Group getGroupById(String id);
}
