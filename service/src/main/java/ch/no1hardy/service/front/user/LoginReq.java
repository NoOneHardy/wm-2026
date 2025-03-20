package ch.no1hardy.service.front.user;

import lombok.Data;

@Data
public class LoginReq {
    private String username;
    private String password;
}
