package ch.no1hardy.service.front.group;

import java.util.List;

public record CardGroupRes(
        String id,
        String name,
        Double percentage,
        Double percentageResult,
        List<String> thumbnail,
        Boolean isKnockout,
        Integer order
) {
    public CardGroupRes {
        if (order == null) {
            order = 0;
        }
    }
}
