package ch.no1hardy.service.service;

import ch.no1hardy.service.model.game.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private GameRepository repository;

    @Test
    @DisplayName("getOpenBets() - should return empty list when no games are available")
    void shouldReturnEmptyListWhenNoGamesAvailable() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should throw BadRequestException when no user is logged in")
    void shouldThrowBadRequestExceptionWhenNoUserLoggedIn() {
        // TODO
        return;
    }

    @Test
    @DisplayName("getOpenBets() - should return list of open bets for the user")
    void shouldReturnListOfOpenBetsForUser() {
        // TODO
        return;
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
