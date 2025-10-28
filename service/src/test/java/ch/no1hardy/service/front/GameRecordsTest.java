package ch.no1hardy.service.front;

import ch.no1hardy.service.front.game.BetRes;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreRes;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Game-related records.
 * Verifies that the records behave correctly as immutable data carriers.
 */
class GameRecordsTest {

    @Test
    void shouldCreateBetResRecord() {
        BetRes bet = new BetRes("bet-1", "game-1", 2, 1, 2);
        
        assertEquals("bet-1", bet.id());
        assertEquals("game-1", bet.gameId());
        assertEquals(2, bet.scoreTeamHome());
        assertEquals(1, bet.scoreTeamGuest());
        assertEquals(2, bet.joker());
    }

    @Test
    void shouldCreateScoreResRecord() {
        ScoreRes score = new ScoreRes("score-1", "game-1", 3, 2);
        
        assertEquals("score-1", score.id());
        assertEquals("game-1", score.gameId());
        assertEquals(3, score.scoreTeamHome());
        assertEquals(2, score.scoreTeamGuest());
    }

    @Test
    void shouldCreateGameReqRecord() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 6, 14, 20, 0);
        GameReq gameReq = new GameReq(timestamp, "group-1", "team-1", "team-2");
        
        assertEquals(timestamp, gameReq.timestamp());
        assertEquals("group-1", gameReq.group());
        assertEquals("team-1", gameReq.teamHome());
        assertEquals("team-2", gameReq.teamGuest());
    }

    @Test
    void shouldSupportEqualityForBetRes() {
        BetRes bet1 = new BetRes("bet-1", "game-1", 2, 1, 2);
        BetRes bet2 = new BetRes("bet-1", "game-1", 2, 1, 2);
        BetRes bet3 = new BetRes("bet-2", "game-1", 2, 1, 2);
        
        assertEquals(bet1, bet2);
        assertNotEquals(bet1, bet3);
        assertEquals(bet1.hashCode(), bet2.hashCode());
    }

    @Test
    void shouldHandleNullScores() {
        BetRes bet = new BetRes("bet-1", "game-1", null, null, 1);
        
        assertNull(bet.scoreTeamHome());
        assertNull(bet.scoreTeamGuest());
        assertEquals(1, bet.joker());
    }
}
