package ch.no1hardy.service.front.group;

import java.util.List;

public record OverviewRes(
        Double percentage,
        Double percentageResult,
        List<CardGroupRes> groups
) {
}
