package ch.no1hardy.service.front.team;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamRes {
    private String id;
    private String name;
    private String flag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
