package ch.no1hardy.service.front.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalStatistics {
    private Integer totalPoints;
    private Integer correctGames;
    private Integer jokersWasted;
}
