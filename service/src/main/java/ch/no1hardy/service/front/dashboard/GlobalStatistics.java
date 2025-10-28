package ch.no1hardy.service.front.dashboard;

public record GlobalStatistics(
        Integer totalPoints,
        Integer correctGames,
        Integer jokersWasted
) {
}
