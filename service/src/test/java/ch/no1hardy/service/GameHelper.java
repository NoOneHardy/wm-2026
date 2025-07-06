package ch.no1hardy.service;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.user.User;

import java.util.ArrayList;
import java.util.List;

public abstract class GameHelper {
    public static Game createGame(String id) {
        Game game = new Game();
        game.setId(id);
        return game;
    }

    public static Score createScore(String id, Game game, int guestScore, int homeScore) {
        Score score = new Score();
        score.setId(id);
        score.setGame(game);
        score.setScoreTeamGuest(guestScore);
        score.setScoreTeamHome(homeScore);
        game.setResult(score);
        return score;
    }

    public static Bet createBet(String id, User user, Game game, int guestScore, int homeScore) {
        return createBet(id, user, game, guestScore, homeScore, 1);
    }

    public static Bet createBet(String id, User user, Game game, int guestScore, int homeScore, int joker) {
        Bet bet = new Bet();
        bet.setId(id);
        bet.setUser(user);
        bet.setGame(game);
        bet.setScoreTeamGuest(guestScore);
        bet.setScoreTeamHome(homeScore);
        bet.setJoker(joker);

        if (game.getBets() == null) game.setBets(List.of(bet));
        else {
            List<Bet> bets = new ArrayList<>();
            bets.add(bet);
            bets.addAll(game.getBets());
            game.setBets(bets);
        }

        if (user.getBets() == null) user.setBets(List.of(bet));
        else {
            List<Bet> bets = new ArrayList<>();
            bets.add(bet);
            bets.addAll(user.getBets());
            user.setBets(bets);
        }

        return bet;
    }
}
