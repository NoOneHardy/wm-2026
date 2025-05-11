package ch.no1hardy.service.service;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    void shouldCalculatePointsForCorrectBet() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);

        Score score = new Score();
        score.setScoreTeamGuest(2);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 100);

        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 100);

        bet.setScoreTeamGuest(0);
        bet.setScoreTeamHome(0);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(0);
        checkPoints(bet, score, 100);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 70);


        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 70);
    }

    @Test
    void shouldCalculatePointsForTieWithIncorrectGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 50);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 60);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 60);
    }

    @Test
    void shouldCalculatePointsForCorrectWinnerWithoutAnyBonus() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(4);

        checkPoints(bet, score, 50);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(4);
        score.setScoreTeamHome(2);

        checkPoints(bet, score, 50);
    }

    @Test
    void shouldCalculatePointsForCorrectGoalsOfOneTeam() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(1);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 20);

        bet.setScoreTeamGuest(2);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(1);

        checkPoints(bet, score, 20);
    }

    @Test
    void shouldCalculatePointsForCorrectTotalGoals() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(3);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 10);

        bet.setScoreTeamGuest(4);
        bet.setScoreTeamHome(1);
        score.setScoreTeamGuest(2);
        score.setScoreTeamHome(3);

        checkPoints(bet, score, 10);
    }

    @Test
    void shouldCalculatePointsForNoBonus() {
        Bet bet = new Bet();
        bet.setScoreTeamGuest(1);
        bet.setScoreTeamHome(2);

        Score score = new Score();
        score.setScoreTeamGuest(0);
        score.setScoreTeamHome(0);

        checkPoints(bet, score, 0);
    }

    Method getCalculateUserPointsMethod() throws NoSuchMethodException {
        Method method = UserService.class.getDeclaredMethod("calculateUserPoints", Bet.class, Score.class);
        method.setAccessible(true);
        return method;
    }

    void checkPoints(Bet bet, Score score, int expectedPoints)  {
        try {
            bet.setJoker(1);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        try {
            bet.setJoker(2);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints * 2, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        try {
            bet.setJoker(3);
            int points = (int) getCalculateUserPointsMethod().invoke(service, bet, score);
            assertEquals(expectedPoints * 3, points);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }
}
