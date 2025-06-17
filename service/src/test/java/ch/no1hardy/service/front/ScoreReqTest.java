package ch.no1hardy.service.front;

import ch.no1hardy.service.front.game.ScoreReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreReqTest {
    @Test
    @DisplayName("should validate score request with valid scores")
    public void shouldValidateScoreRequestWithValidScores() {
        ScoreReq scoreReq = ScoreReq.builder()
                .game("game-1")
                .scoreTeamHome(2)
                .scoreTeamGuest(3)
                .build();

        assertTrue(scoreReq.isValid());
    }

    @Test
    @DisplayName("should validate score request with null values")
    public void shouldValidateScoreRequestWithNullValues() {
        ScoreReq scoreReq = ScoreReq.builder()
                .game("game-1")
                .scoreTeamHome(null)
                .scoreTeamGuest(1)
                .build();

        assertFalse(scoreReq.isValid());
        scoreReq.setScoreTeamHome(1);
        scoreReq.setScoreTeamGuest(null);
        assertFalse(scoreReq.isValid());
    }

    @Test
    @DisplayName("should validate score request with negative values")
    public void shouldValidateScoreRequestWithNegativeValues() {
        ScoreReq scoreReq = ScoreReq.builder()
                .game("game-1")
                .scoreTeamHome(-4)
                .scoreTeamGuest(1)
                .build();

        assertFalse(scoreReq.isValid());
        scoreReq.setScoreTeamHome(1);
        scoreReq.setScoreTeamGuest(-4);
        assertFalse(scoreReq.isValid());
    }
}
