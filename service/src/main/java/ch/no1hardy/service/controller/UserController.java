package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public List<UserRes> list() {
        logger.info("GET /user");
        return this.service.list();
    }

    @PostMapping()
    public UserRes create(@RequestBody UserReq dto) {
        logger.info("POST /user");
        return this.service.create(dto);
    }
}
