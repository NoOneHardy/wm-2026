package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {
    private final TeamService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public TeamRes create(@RequestBody TeamReq dto) {
        return service.create(dto);
    }
}
