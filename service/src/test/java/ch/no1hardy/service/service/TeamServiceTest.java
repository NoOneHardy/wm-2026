package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.MissingRequestBodyException;
import ch.no1hardy.service.exception.MissingRequestPropertyException;
import ch.no1hardy.service.front.team.TeamReq;
import ch.no1hardy.service.front.team.TeamRes;
import ch.no1hardy.service.model.team.Team;
import ch.no1hardy.service.model.team.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeamServiceTest {
    @MockitoBean
    private TeamRepository repository;

    @Autowired
    private TeamService teamService;

    @BeforeEach
    void beforeEach() {
        Team mockTeam = new Team();
        mockTeam.setFlag("flag-1");
        mockTeam.setName("Team 1");
        mockTeam.setId("team-1");
        mockTeam.setShortName("T1");
        mockTeam.setGamesHome(List.of());
        mockTeam.setGamesGuest(List.of());

        when(repository.save(any(Team.class))).thenReturn(mockTeam);
    }

    @Test
    void shouldCreateAndMapTeamFromDto() {
        TeamReq req = new TeamReq();
        req.setFlag("flag-1");
        req.setName("Team 1");
        req.setShortName("T1");

        TeamRes res = teamService.create(req);
        assertEquals("flag-1", res.getFlag());
        assertEquals("Team 1", res.getName());
        assertEquals("T1", res.getShortName());
        assertEquals("team-1", res.getId());
    }

    @Test
    void shouldThrowErrorIfDtoIsNull() {
        MissingRequestBodyException e = assertThrows(MissingRequestBodyException.class, () -> {
            // Call create method with null
            teamService.create(null);
        });
        assertEquals("Missing request body", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    void shouldThrowErrorIfPropertyIsMissing() {
        TeamReq req = new TeamReq();

        MissingRequestPropertyException eName = assertThrows(MissingRequestPropertyException.class, () -> {
            // Call create method with empty dto
            teamService.create(req);
        });
        assertEquals("Request body is missing property 'name' of type 'string'", eName.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, eName.getStatus());

        // Set name so flag is the only null value
        req.setName("Team 1");

        MissingRequestPropertyException eFlag = assertThrows(MissingRequestPropertyException.class, () -> {
            // Call create method with empty dto
            teamService.create(req);
        });
        assertEquals("Request body is missing property 'flag' of type 'string'", eFlag.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, eFlag.getStatus());
    }
}
