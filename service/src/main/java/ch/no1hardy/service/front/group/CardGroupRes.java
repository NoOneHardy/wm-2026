package ch.no1hardy.service.front.group;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CardGroupRes {
    private String id;
    private String name;
    private Double percentage;
    private Double percentageResult;
    private List<String> thumbnail;
    private Boolean isKnockout;
    @Builder.Default
    private Integer order = 0;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
