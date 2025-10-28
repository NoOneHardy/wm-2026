package ch.no1hardy.service.front.dashboard;

public record UserSummary(
        Integer points,
        Integer ranking,
        Double percentage,
        Boolean isConfirmed
) {
}
