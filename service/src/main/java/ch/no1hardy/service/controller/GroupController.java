package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService service;

    @GetMapping("/{id}")
    public GroupRes getGroup(@PathVariable("id") String id) {
        return service.getGroup(id);
    }
}
