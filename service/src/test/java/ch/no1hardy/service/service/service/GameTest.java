package ch.no1hardy.service.service.service;

import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameTest {
    Game game;

    @BeforeEach
    void beforeEach() {
        game = new Game();
        game.setId("game-0");
    }

    @Test
    void  shouldReturnWhetherIsInKnockoutGroup() {
        Group group = new Group();
        group.setIsKnockout(true);
        game.setGroup(group);

        assertEquals(true, game.isKnockout());

        group.setIsKnockout(false);
        assertEquals(false, game.isKnockout());
    }

    @Test
    void shouldReturnFalseIfGroupIsNull() {
        assertNull(game.getGroup());
        assertEquals(false, game.isKnockout());
    }

    @Test
    void shouldReturnWhetherGameIsInGroupPhase() {
        Group group = new Group();
        group.setIsKnockout(false);
        game.setGroup(group);

        assertEquals(true, game.isGroupPhase());

        group.setIsKnockout(true);
        assertEquals(false, game.isGroupPhase());
    }

    @Test
    void shouldReturnTrueIfGroupIsNull() {
        assertNull(game.getGroup());
        assertEquals(true, game.isGroupPhase());
    }

    @Test
    void shouldReturnWhetherGameHasResult() {
        assertNull(game.getResult());
        assertEquals(false, game.hasResult());

        Score result = new Score();
        game.setResult(result);

        assertEquals(result, game.getResult());
        assertEquals(true, game.hasResult());
    }
}
