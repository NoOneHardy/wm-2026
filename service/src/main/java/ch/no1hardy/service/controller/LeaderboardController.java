package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
    private final UserService userService;

    @GetMapping()
    public List<RankingRes> getLeaderboard() {
        return userService.getLeaderboard();
    }
}
