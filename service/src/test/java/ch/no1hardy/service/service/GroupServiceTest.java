package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.KnockoutTieException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.game.ResultReq;
import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.front.group.GroupOptionRes;
import ch.no1hardy.service.front.group.GroupRes;
import ch.no1hardy.service.mapper.GroupMapperImpl;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceTest {
    @Autowired
    private GroupService service;

    @MockitoBean
    private GroupRepository repository;
    @MockitoBean
    private GroupMapperImpl mapper;
    @MockitoBean
    private GameService gameService;

    @BeforeEach
    void beforeEach() {
        Group mockGroup = new Group();
        mockGroup.setId("group-1");

        Game game = new Game();
        game.setId("game-1");
        game.setTimestamp(LocalDateTime.now().plusDays(1));
        mockGroup.setGames(List.of(game));

        when(repository.findAll()).thenReturn(List.of(mockGroup));
        when(mapper.toCard(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(CardGroupRes.builder().build());
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
    void shouldListGroupOptionsSortedByOrderAndIgnoreDeletedEntries() {
        Group knockout = new Group();
        knockout.setId("group-2");
        knockout.setName("Halbfinale");
        knockout.setIsKnockout(true);
        knockout.setOrder(2);
        knockout.setThumbnail("/cdn/knockout.png");

        Group active = new Group();
        active.setId("group-1");
        active.setName("Gruppe A");
        active.setIsKnockout(false);
        active.setOrder(1);

        Group deleted = new Group();
        deleted.setId("group-3");
        deleted.setName("Gruppe B");
        deleted.setOrder(3);
        deleted.delete();

        when(repository.findAll()).thenReturn(List.of(knockout, active, deleted));

        List<GroupOptionRes> options = service.listOptions();
        assertEquals(2, options.size());
        assertEquals("group-1", options.getFirst().getId());
        assertEquals("group-2", options.get(1).getId());
        assertEquals("/cdn/knockout.png", options.get(1).getThumbnail());
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

    // --- updateResults ---

    @Test
    @DisplayName("updateResults() - should throw NotFoundException when group does not exist")
    void updateResults_shouldThrowNotFoundExceptionWhenGroupNotFound() {
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateResults("nonexistent", List.of()));
    }

    @Test
    @DisplayName("updateResults() - should skip results with negative scores")
    void updateResults_shouldSkipInvalidResults() {
        Group group = groupWith("group-1", List.of(activeGame("game-1")));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));

        service.updateResults("group-1", List.of(resultReq("game-1", -1, 0)));

        verify(gameService, never()).uploadResult(any(), any());
    }

    @Test
    @DisplayName("updateResults() - should skip results with no matching game in group")
    void updateResults_shouldSkipResultsWithNoMatchingGame() {
        Group group = groupWith("group-1", List.of(activeGame("game-1")));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));

        service.updateResults("group-1", List.of(resultReq("game-999", 1, 0)));

        verify(gameService, never()).uploadResult(any(), any());
    }

    @Test
    @DisplayName("updateResults() - should skip results for inactive (deleted) games")
    void updateResults_shouldSkipResultsForInactiveGames() {
        Game deletedGame = new Game();
        deletedGame.setId("game-1");
        deletedGame.delete();

        Group group = groupWith("group-1", List.of(deletedGame));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));

        service.updateResults("group-1", List.of(resultReq("game-1", 1, 0)));

        verify(gameService, never()).uploadResult(any(), any());
    }

    @Test
    @DisplayName("updateResults() - should upload result for a matching active game without existing result")
    void updateResults_shouldUploadResultForMatchingActiveGame() {
        Group group = groupWith("group-1", List.of(activeGame("game-1")));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));
        ResultReq result = resultReq("game-1", 2, 1);

        service.updateResults("group-1", List.of(result));

        verify(gameService, times(1)).uploadResult("game-1", result);
    }

    @Test
    @DisplayName("updateResults() - should skip upload when active result scores are identical")
    void updateResults_shouldSkipWhenActiveResultHasIdenticalScores() {
        Game game = activeGame("game-1");
        game.setResult(activeScore(2, 1));

        Group group = groupWith("group-1", List.of(game));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));

        service.updateResults("group-1", List.of(resultReq("game-1", 2, 1)));

        verify(gameService, never()).uploadResult(any(), any());
    }

    @Test
    @DisplayName("updateResults() - should upload result when active result scores differ")
    void updateResults_shouldUploadWhenActiveResultScoresDiffer() {
        Game game = activeGame("game-1");
        game.setResult(activeScore(2, 1));

        Group group = groupWith("group-1", List.of(game));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));
        ResultReq result = resultReq("game-1", 3, 0);

        service.updateResults("group-1", List.of(result));

        verify(gameService, times(1)).uploadResult("game-1", result);
    }

    @Test
    @DisplayName("updateResults() - should upload result when existing result is inactive")
    void updateResults_shouldUploadWhenExistingResultIsInactive() {
        Score inactiveScore = activeScore(2, 1);
        inactiveScore.delete();

        Game game = activeGame("game-1");
        game.setResult(inactiveScore);

        Group group = groupWith("group-1", List.of(game));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));
        ResultReq result = resultReq("game-1", 2, 1);

        service.updateResults("group-1", List.of(result));

        verify(gameService, times(1)).uploadResult("game-1", result);
    }

    @Test
    @DisplayName("updateResults() - should rethrow KnockoutTieException with German display message")
    void updateResults_shouldRethrowKnockoutTieExceptionWithGermanMessage() {
        Group group = groupWith("group-1", List.of(activeGame("game-1")));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));
        doThrow(new KnockoutTieException("tie", "original")).when(gameService).uploadResult(any(), any());

        KnockoutTieException ex = assertThrows(KnockoutTieException.class,
                () -> service.updateResults("group-1", List.of(resultReq("game-1", 1, 1))));

        assertEquals("Diese Gruppe ist eine K.O.-Phase. Spiele können nicht unentschieden enden.", ex.getDisplayMessage());
    }

    @Test
    @DisplayName("updateResults() - should process multiple results independently and return mapped DTO")
    void updateResults_shouldProcessMultipleResultsIndependently() {
        Game game1 = activeGame("game-1");
        Game game2 = activeGame("game-2");
        game2.setResult(activeScore(1, 0));

        Group group = groupWith("group-1", List.of(game1, game2));
        when(repository.findById("group-1")).thenReturn(Optional.of(group));

        GroupRes expected = GroupRes.builder().id("group-1").build();
        when(mapper.toDto(group)).thenReturn(expected);

        ResultReq result1 = resultReq("game-1", 2, 0);
        ResultReq result2 = resultReq("game-2", 1, 0); // identical scores → skipped

        GroupRes actual = service.updateResults("group-1", List.of(result1, result2));

        verify(gameService, times(1)).uploadResult("game-1", result1);
        verify(gameService, never()).uploadResult(eq("game-2"), any());
        assertEquals(expected, actual);
    }

    // --- helpers ---

    private Game activeGame(String id) {
        Game game = new Game();
        game.setId(id);
        return game;
    }

    private Score activeScore(int home, int guest) {
        Score score = new Score();
        score.setScoreTeamHome(home);
        score.setScoreTeamGuest(guest);
        return score;
    }

    private ResultReq resultReq(String gameId, int home, int guest) {
        ResultReq req = new ResultReq();
        req.setGame(gameId);
        req.setScoreTeamHome(home);
        req.setScoreTeamGuest(guest);
        return req;
    }

    private Group groupWith(String id, List<Game> games) {
        Group group = new Group();
        group.setId(id);
        group.setGames(games);
        return group;
    }

}
