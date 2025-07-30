package ch.no1hardy.service.mapper;

import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserHelperTest {
    @MockitoBean
    private UserService service;

    @Autowired
    private UserHelper userHelper;

    private final User user = new User();

    @Test
    void shouldCalculateAvailable2Jokers() {
        Bet bet = new Bet();
        bet.setJoker(2);
        Bet bet2 = new Bet();
        bet2.setJoker(2);

        user.setBets(List.of(bet, bet2));

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableDoubleJokers(8)).isEqualTo(6);
    }

    @Test
    void shouldReturnMax2JokersIfNoBetsWereFound() {
        user.setBets(List.of());

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableDoubleJokers(8)).isEqualTo(8);
    }

    @Test
    void shouldReturnMax2JokersIfNoUserIsFound() {
        when(service.getCurrentUserRaw()).thenReturn(Optional.empty());
        assertThat(userHelper.getAvailableDoubleJokers(2)).isEqualTo(2);
    }

    @Test
    void shouldIgnore1And3Jokers() {
        Bet joker2 = new Bet();
        joker2.setJoker(2);

        Bet joker1 = new Bet();
        joker1.setJoker(1);

        Bet joker3 = new Bet();
        joker3.setJoker(3);

        user.setBets(List.of(joker1, joker2, joker3));

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableDoubleJokers(8)).isEqualTo(7);
    }

    @Test
    void shouldCalculateAvailable3Jokers() {
        Bet bet = new Bet();
        bet.setJoker(3);
        Bet bet2 = new Bet();
        bet2.setJoker(3);

        user.setBets(List.of(bet, bet2));

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableTripleJokers(6)).isEqualTo(4);
    }

    @Test
    void shouldReturnMax3JokersIfNoBetsWereFound() {
        user.setBets(List.of());

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableTripleJokers(6)).isEqualTo(6);
    }

    @Test
    void shouldReturnMax3JokersIfNoUserIsFound() {
        when(service.getCurrentUserRaw()).thenReturn(Optional.empty());
        assertThat(userHelper.getAvailableTripleJokers(3)).isEqualTo(3);
    }

    @Test
    void shouldIgnore1And2Jokers() {
        Bet joker2 = new Bet();
        joker2.setJoker(2);

        Bet joker1 = new Bet();
        joker1.setJoker(1);

        Bet joker3 = new Bet();
        joker3.setJoker(3);

        user.setBets(List.of(joker1, joker2, joker3));

        when(service.getCurrentUserRaw()).thenReturn(Optional.of(user));

        assertThat(userHelper.getAvailableTripleJokers(4)).isEqualTo(3);
    }
}
