package ch.no1hardy.service.front;

import ch.no1hardy.service.front.game.ResultReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultReqTest {
    @Test
    @DisplayName("should validate score request with valid scores")
    public void shouldValidateScoreRequestWithValidScores() {
        ResultReq req = new ResultReq();
        req.setGame("game-1");
        req.setScoreTeamHome(2);
        req.setScoreTeamGuest(3);

        assertTrue(req.isValid());
    }

    @Test
    @DisplayName("should validate score request with null values")
    public void shouldValidateScoreRequestWithNullValues() {
        ResultReq req = new ResultReq();
        req.setGame("game-1");
        req.setScoreTeamHome(null);
        req.setScoreTeamGuest(1);

        assertTrue(req.isValid());
        req.setScoreTeamHome(1);
        req.setScoreTeamGuest(null);
        assertTrue(req.isValid());
    }

    @Test
    @DisplayName("should validate score request with negative values")
    public void shouldValidateScoreRequestWithNegativeValues() {
        ResultReq req = new ResultReq();
        req.setGame("game-1");
        req.setScoreTeamHome(-4);
        req.setScoreTeamGuest(1);

        assertFalse(req.isValid());
        req.setScoreTeamHome(1);
        req.setScoreTeamGuest(-4);
        assertFalse(req.isValid());
    }
}
