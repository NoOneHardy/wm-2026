package ch.no1hardy.service.front.leaderboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingRes {
    private String id;
    private String username;
    private String avatar;
    private Integer points;
    private Integer prevRanking;
    private Integer ranking;
}
