package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/user/{id}/confirm")
    public UserRes confirmUser(@PathVariable("id") String id) {
        return userService.confirmUser(id);
    }

    @GetMapping("/user/{id}/deny")
    public UserRes denyUser(@PathVariable("id") String id) {
        return userService.denyUser(id);
    }
}
