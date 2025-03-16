package ch.no1hardy.service.front.user;

import lombok.Data;

@Data
public class UserReq {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String avatar;
}
