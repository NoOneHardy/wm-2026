package ch.no1hardy.service.front;

import ch.no1hardy.service.front.game.BetReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BetReqTest {
    @Test
    @DisplayName("should validate bet request with valid scores")
    public void shouldValidateBetRequestWithValidScores() {
        BetReq betReq = new BetReq();
        betReq.setGame("game-1");
        betReq.setScoreTeamHome(2);
        betReq.setScoreTeamGuest(3);
        betReq.setJoker(1);

        assertTrue(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with null values")
    public void shouldValidateBetRequestWithNullValues() {
        BetReq betReq = new BetReq();
        betReq.setGame("game-1");
        betReq.setScoreTeamHome(null);
        betReq.setScoreTeamGuest(1);
        betReq.setJoker(1);

        assertTrue(betReq.isValid());
        betReq.setScoreTeamHome(1);
        betReq.setScoreTeamGuest(null);
        assertTrue(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with negative values")
    public void shouldValidateBetRequestWithNegativeValues() {
        BetReq betReq = new BetReq();
        betReq.setGame("game-1");
        betReq.setScoreTeamHome(-4);
        betReq.setScoreTeamGuest(1);
        betReq.setJoker(1);

        assertFalse(betReq.isValid());
        betReq.setScoreTeamHome(1);
        betReq.setScoreTeamGuest(-4);
        assertFalse(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with null joker")
    public void shouldValidateBetRequestWithNullJoker() {
        BetReq betReq = new BetReq();
        betReq.setGame("game-1");
        betReq.setScoreTeamHome(4);
        betReq.setScoreTeamGuest(1);
        betReq.setJoker(null);

        assertFalse(betReq.isValid());
        betReq.setJoker(2);
        assertTrue(betReq.isValid());
    }
}
