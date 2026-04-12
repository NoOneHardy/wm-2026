package ch.no1hardy.service.model;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BetTest {
    Bet bet;
    Game game;
    Score score;

    @BeforeEach
    void beforeEach() {
        bet = new Bet();
        bet.setId("bet-1");
        bet.setScoreTeamHome(2);
        bet.setScoreTeamGuest(3);

        game = new Game();
        bet.setGame(game);
        game.setBets(List.of(bet));

        game.setId("game-1");

        score = new Score();
        game.setResult(score);
        score.setGame(game);

        score.setScoreTeamHome(2);
        score.setScoreTeamGuest(3);
    }

    @Test
    @DisplayName("isCorrect() - should return true if bet matches game result")
    void shouldReturnTrueIfBetMatchesGameResult() {
        assertTrue(bet.isCorrect());
    }

    @Test
    @DisplayName("isCorrect() - should return false if bet does not match game result")
    void shouldReturnFalseIfBetDoesNotMatchGameResult() {
        score.setScoreTeamHome(1);
        score.setScoreTeamGuest(4);
        assertFalse(bet.isCorrect());
    }

    @Test
    @DisplayName("isCorrect() - should return false if result is null")
    void shouldReturnFalseIfBetIsNull() {
        game.setResult(null);
        assertFalse(bet.isCorrect());
    }
}
