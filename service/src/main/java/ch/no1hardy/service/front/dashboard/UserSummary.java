package ch.no1hardy.service.front.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {
    private Integer points;
    private Integer ranking;
    private Double percentage;
    private Boolean isConfirmed;
}
