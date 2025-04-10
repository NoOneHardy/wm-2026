package ch.no1hardy.service.service;

import ch.no1hardy.service.front.group.GroupRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {
    public GroupRes getGroup(String id) {
        return new GroupRes();
    }
}
