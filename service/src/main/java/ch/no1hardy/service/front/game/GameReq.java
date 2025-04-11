package ch.no1hardy.service.front.game;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameReq {
    private LocalDateTime timestamp;
    private String group;
    private String teamHome;
    private String teamGuest;
}
