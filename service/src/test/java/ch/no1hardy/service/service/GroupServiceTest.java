package ch.no1hardy.service.service;

import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.mapper.GroupMapperImpl;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GroupServiceTest {
    @Autowired
    private GroupService service;

    @MockitoBean
    private GroupRepository repository;
    @MockitoBean
    private GroupMapperImpl mapper;

    @BeforeEach
    void beforeEach() {
        Group mockGroup = new Group();
        mockGroup.setId("group-1");

        Game game = new Game();
        game.setId("game-1");
        game.setTimestamp(LocalDateTime.now().plusDays(1));
        mockGroup.setGames(List.of(game));

        when(repository.findAll()).thenReturn(List.of(mockGroup));
        when(mapper.toCard(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new CardGroupRes(null, null, null, null, null, null, 0));
    }

    @Test
    void shouldFilterCardGroupsWithGames() {
        List<CardGroupRes> groups = service.getCardGroups();
        assertEquals(1, groups.size());

        Group mockGroup = new Group();
        mockGroup.setId("group-2");
        mockGroup.setGames(List.of());

        when(repository.findAll()).thenReturn(List.of(mockGroup));
        assertEquals(0, service.getCardGroups().size());
    }

    @Test
    void shouldIgnoreDeletedEntries() {
        Group group1 = new Group();
        group1.setId("group-1");
        group1.setDeletedAt(LocalDateTime.now());

        Group group2 = new Group();
        group2.setId("group-2");

        Game game = new Game();
        game.setId("game-1");
        game.setTimestamp(LocalDateTime.now().plusDays(1));
        group2.setGames(List.of(game));
        group1.setGames(List.of(game));

        when(repository.findAll()).thenReturn(List.of(group1, group2));
        assertEquals(1, service.getCardGroups().size());
        assertEquals(CardGroupRes.class, service.getCardGroups().getFirst().getClass());
    }

    @Test
    @DisplayName("getOverallPercentage() - should return 0.0 if there are no games")
    void shouldReturnZeroIfNoGames() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Group group = new Group();
        group.setGames(List.of());
        when(repository.findAll()).thenReturn(List.of(group));

        assertEquals(0.0, service.getOverallPercentage(user));

        when(repository.findAll()).thenReturn(List.of());
        assertEquals(0.0, service.getOverallPercentage(user));
    }

    @Test
    @DisplayName("getOverallPercentage() - should return 0.0 if user has no bets")
    void shouldReturnZeroIfUserHasNoBets() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of());

        Group group = new Group();
        group.setGames(List.of(
                new Game(),
                new Game(),
                new Game(),
                new Game()
        ));
        when(repository.findAll()).thenReturn(List.of(group));

        assertEquals(0.0, service.getOverallPercentage(user));
    }

    @Test
    @DisplayName("getOverallPercentage() - should return correct percentage")
    void shouldReturnCorrectPercentage() {
        User user = new User();
        user.setId("user-1");
        user.setBets(List.of(
                new Bet(),
                new Bet(),
                new Bet()
        ));

        Group group = new Group();
        group.setGames(List.of(
                new Game(),
                new Game(),
                new Game(),
                new Game()
        ));
        when(repository.findAll()).thenReturn(List.of(group));

        Double percentage = service.getOverallPercentage(user);
        assertEquals(75.0, percentage);
    }

}
