package ch.no1hardy.service.service;

import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.group.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    void beforeEach() {
        Group mockGroup = new Group();
        mockGroup.setId("group-1");

        Game game = new Game();
        game.setId("game-1");
        game.setTimestamp(LocalDateTime.now());
        mockGroup.setGames(List.of(game));

        when(repository.findAll()).thenReturn(List.of(mockGroup));
    }

    @Test
    void shouldFilterCardGroupsWithGames() {
        List<CardGroupRes> groups = service.getCardGroups();
        assertEquals(1, groups.size());
        assertEquals("group-1", groups.getFirst().getId());

        Group mockGroup = new Group();
        mockGroup.setId("group-2");
        mockGroup.setGames(List.of());

        when(repository.findAll()).thenReturn(List.of(mockGroup));
        assertEquals(0, service.getCardGroups().size());
    }
}
