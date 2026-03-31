package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.ResultReq;
import ch.no1hardy.service.front.group.GroupOptionRes;
import ch.no1hardy.service.front.group.GroupReq;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.front.group.OverviewRes;
import ch.no1hardy.service.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService service;

    @GetMapping()
    public OverviewRes getGroups() {
        return service.getOverview();
    }

    @GetMapping("/{id}")
    public GroupRes getGroup(@PathVariable("id") String id) {
        return service.getGroup(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<GroupOptionRes> listAll() {
        return service.listOptions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public GroupRes create(@RequestBody GroupReq dto) {
        return service.create(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/bets")
    public GroupRes updateBets(@PathVariable("id") String id, @RequestBody List<BetReq> bets) {
        return service.updateBets(id, bets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/results")
    public GroupRes updateResults(@PathVariable("id") String id, @RequestBody List<ResultReq> results) {
        return service.updateResults(id, results);
    }
}
