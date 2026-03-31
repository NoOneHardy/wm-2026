package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {
    private final TeamService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<TeamRes> list() {
        return service.list();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public TeamRes create(@RequestBody TeamReq dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/flag")
    public String uploadFlag(@RequestParam("file") MultipartFile flag) {
        return service.uploadFlag(flag);
    }
}
