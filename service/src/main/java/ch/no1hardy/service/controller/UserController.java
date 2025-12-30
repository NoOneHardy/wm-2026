package ch.no1hardy.service.controller;

import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.LoginReq;
import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.front.verification.ResetPasswordReq;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.service.JwtService;
import ch.no1hardy.service.service.NotificationService;
import ch.no1hardy.service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JwtService jwtService;

    @DeleteMapping("/notification/{id}")
    @PreAuthorize("isAuthenticated()")
    public boolean deleteNotification(@PathVariable String id) {
        logger.info("DELETE /notification/{}", id);
        this.notificationService.markNotificationAsRead(id);
        return true;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/all")
    public List<UserRes> listAll() {
        logger.info("GET /user/all");
        return this.service.listAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public UserRes get(@PathVariable String id) {
        logger.info("GET /user/{}", id);
        return this.service.get(id);
    }

    @GetMapping("/me")
    public UserRes get() {
        logger.info("GET /me");
        return this.service.getCurrentUser();
    }

    @PostMapping("/signup")
    public UserRes create(@RequestBody UserReq dto) {
        logger.info("POST /user");
        return this.service.create(dto);
    }

    @PostMapping("/login")
    @ResponseBody
    public UserRes login(@RequestBody LoginReq dto, HttpServletResponse response) {
        logger.info("POST /login");
        UserRes user = this.service.login(dto);

        response.addCookie(jwtService.generateJwtCookie(user.getId()));
        return user;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/log-out")
    @ResponseBody
    public Boolean logout(HttpServletResponse response) {
        logger.info("POST /log-out");
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return true;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}")
    public UserRes update(@PathVariable String id, @RequestBody UserReq dto) {
        logger.info("PUT /user/{}", id);
        return this.service.update(id, dto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me/avatar")
    public UserRes updateAvatar(@RequestParam("file") MultipartFile avatar) {
        logger.info("PUT /me/avatar");
        return this.service.updateAvatar(avatar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public UserRes delete(@PathVariable String id) {
        logger.info("DELETE /user/{}", id);
        return this.service.delete(id);
    }

    @PostMapping("/user/confirm")
    public UserRes confirmEmail(@RequestBody VerifyEmailReq verifyEmailReq) {
        logger.info("POST /user/confirm");
        return this.service.verifyEmail(verifyEmailReq);
    }

    @PutMapping("/user/resend-verification")
    public boolean resendVerificationEmail() {
        logger.info("PUT /user/resend-verification");
        return this.service.sendEmailVerificationMail();
    }

    @PutMapping("/user/password-reset/request-link")
    public boolean requestPasswordResetLink(@RequestParam String email) {
        logger.info("PUT /user/password-reset/request-link");
        return this.service.sendPasswordResetMail(email);
    }

    @PutMapping("/user/password-reset")
    public boolean resetPassword(@RequestBody ResetPasswordReq resetPasswordReq) {
        logger.info("PUT /user/password-reset");
        return this.service.resetPassword(resetPasswordReq);
    }
}
