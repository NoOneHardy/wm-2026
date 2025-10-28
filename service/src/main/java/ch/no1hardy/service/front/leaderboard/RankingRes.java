package ch.no1hardy.service.front.leaderboard;

public record RankingRes(
        String id,
        String username,
        String avatar,
        Integer points,
        Integer prevRanking,
        Integer ranking
) {
}
