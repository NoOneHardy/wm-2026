package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.dashboard.DashboardData;
import ch.no1hardy.service.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public DashboardData getDashboardData() {
        return service.getDashboard();
    }
}
