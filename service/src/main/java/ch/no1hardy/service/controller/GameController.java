package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {
    private final GameService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public BetGameRes create(@RequestBody GameReq dto) {
        return service.create(dto);
    }

    @GetMapping()
    public List<BetGameRes> listAll() {
        return service.list();
    }

    @PutMapping("{id}/result")
    @PreAuthorize("hasRole('ADMIN')")
    public BetGameRes uploadResult(@PathVariable("id") String id, @RequestBody ScoreReq dto) {
        return service.uploadResult(id, dto);
    }

    @PutMapping("{id}/bet")
    @PreAuthorize("isAuthenticated()")
    public BetGameRes uploadBet(@PathVariable("id") String id, @RequestBody BetReq dto) {
        return service.uploadBet(id, dto);
    }
}
