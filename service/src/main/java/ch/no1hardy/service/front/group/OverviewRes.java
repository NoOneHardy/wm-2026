package ch.no1hardy.service.front.group;

import java.util.List;

public record OverviewRes(
        Double percentage,
        List<CardGroupRes> groups
) {
}
