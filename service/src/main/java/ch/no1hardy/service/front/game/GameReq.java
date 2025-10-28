package ch.no1hardy.service.front.game;

import java.time.LocalDateTime;

public record GameReq(
        LocalDateTime timestamp,
        String group,
        String teamHome,
        String teamGuest
) {
}
