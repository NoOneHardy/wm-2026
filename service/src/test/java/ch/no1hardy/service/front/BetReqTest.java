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
        BetReq betReq = BetReq.builder()
                .game("game-1")
                .scoreTeamHome(2)
                .scoreTeamGuest(3)
                .joker(1)
                .build();

        assertTrue(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with null values")
    public void shouldValidateBetRequestWithNullValues() {
        BetReq betReq = BetReq.builder()
                .game("game-1")
                .scoreTeamHome(null)
                .scoreTeamGuest(1)
                .joker(1)
                .build();

        assertTrue(betReq.isValid());
        betReq.setScoreTeamHome(1);
        betReq.setScoreTeamGuest(null);
        assertTrue(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with negative values")
    public void shouldValidateBetRequestWithNegativeValues() {
        BetReq betReq = BetReq.builder()
                .game("game-1")
                .scoreTeamHome(-4)
                .scoreTeamGuest(1)
                .joker(1)
                .build();

        assertFalse(betReq.isValid());
        betReq.setScoreTeamHome(1);
        betReq.setScoreTeamGuest(-4);
        assertFalse(betReq.isValid());
    }

    @Test
    @DisplayName("should validate bet request with null joker")
    public void shouldValidateBetRequestWithNullJoker() {
        BetReq betReq = BetReq.builder()
                .game("game-1")
                .scoreTeamHome(4)
                .scoreTeamGuest(1)
                .joker(null)
                .build();

        assertFalse(betReq.isValid());
        betReq.setJoker(2);
        assertTrue(betReq.isValid());
    }
}
