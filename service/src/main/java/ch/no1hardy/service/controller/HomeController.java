package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.home.HomeRes;
import ch.no1hardy.service.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {
    private final DashboardService dashboardService;

    @GetMapping()
    public HomeRes getHomeData() {
        return dashboardService.getHomeData();
    }
}
