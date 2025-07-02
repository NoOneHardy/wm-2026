package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private GameRepository repository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId("user-1");
    }

    private Bet createBetForUser(User user, Game game) {
        Bet bet = new Bet();
        bet.setId("bet-1");
        bet.setUser(user);
        bet.setGame(game);
        return bet;
    }

    @Test
    @DisplayName("getOpenBets() - should return empty list when no games are available")
    void shouldReturnEmptyListWhenNoGamesAvailable() {
        when(userService.getLoggedInUser()).thenReturn(user);
        when(repository.findAll()).thenReturn(List.of());

        List<BetGameRes> result = service.getOpenBets();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getOpenBets() - should throw BadRequestException when no user is logged in")
    void shouldThrowBadRequestExceptionWhenNoUserLoggedIn() {
        when(userService.getUserLeaderboard()).thenReturn(null);

        assertThrows(BadRequestException.class, service::getOpenBets);
    }

    @Test
    @DisplayName("getOpenBets() - should return list of open bets for the user")
    void shouldReturnListOfOpenBetsForUser() {
        User otherUser = new User();
        otherUser.setId("other-user");

        when(userService.getLoggedInUser()).thenReturn(user);
        Game game1 = new Game();
        game1.setId("game-1");
        game1.setBets(List.of(
                createBetForUser(otherUser, game1)
        ));
        game1.setTimestamp(LocalDateTime.now().plusDays(2));
        Game game2 = new Game();
        game2.setId("game-2");
        game2.setBets(List.of(
                createBetForUser(user, game2),
                createBetForUser(otherUser, game2)
        ));
        game2.setTimestamp(LocalDateTime.now().plusDays(3));
        when(repository.findAll()).thenReturn(List.of(game1, game2));

        List<BetGameRes> result = service.getOpenBets();
        assertEquals(1, result.size());
        assertEquals("game-1", result.getFirst().getId());
    }

    @Test
    @DisplayName("getOpenBets() - should return empty list when user has no open bets")
    void shouldReturnEmptyListWhenUserHasNoOpenBets() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should filter games in the next 5 days")
    void shouldFilterGamesInNextFiveDays() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return only active games")
    void shouldReturnOnlyActiveGames() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return games without a bet")
    void shouldReturnGamesWithoutABet() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return games without a result")
    void shouldReturnGamesWithoutAResult() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return one game if it is the only one that matches the criteria")
    void shouldReturnOneGameIfItIsTheOnlyOneAvailable() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return up to 5 games")
    void shouldReturnUpToFiveGames() {
        // TODO
        return;
    }
}
