package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.user.*;
import ch.no1hardy.service.service.JwtService;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JwtService jwtService;

    @GetMapping("/user")
    public List<UserRes> list() {
        logger.info("GET /user");
        return this.service.list();
    }

    @GetMapping("/user/check")
    public CheckRes check(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email
    ) {
        logger.info("GET /user/check?username={}&email={}", username, email);
        return service.check(username, email);
    }

    @GetMapping("/user/all")
    public List<UserRes> listAll() {
        logger.info("GET /user/all");
        return this.service.listAll();
    }

    @GetMapping("/user/{id}")
    public UserRes get(@PathVariable String id) {
        logger.info("GET /user/{}", id);
        return this.service.get(id);
    }

    @GetMapping("/me")
    public UserRes get() {
        logger.info("GET /me");
        return this.service.getLoggedInUser();
    }

    @PostMapping("/signup")
    public UserRes create(@RequestBody UserReq dto) {
        logger.info("POST /user");
        return this.service.create(dto);
    }

    @PostMapping("/login")
    public LoginRes login(@RequestBody LoginReq dto) {
        logger.info("POST /login");
        UserRes user = this.service.login(dto);
        String jwtToken = jwtService.generateToken(user.getUsername());

        return LoginRes.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    @PutMapping("/user/{id}")
    public UserRes update(@PathVariable String id, @RequestBody UserReq dto) {
        logger.info("PUT /user/{}", id);
        return this.service.update(id, dto);
    }

    @DeleteMapping("/user/{id}")
    public UserRes delete(@PathVariable String id) {
        logger.info("DELETE /user/{}", id);
        return this.service.delete(id);
    }
}
