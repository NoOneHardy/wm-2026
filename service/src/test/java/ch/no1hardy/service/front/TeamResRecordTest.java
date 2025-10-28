package ch.no1hardy.service.front;

import ch.no1hardy.service.front.team.TeamRes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TeamRes record.
 * Verifies that the record behaves correctly as an immutable data carrier.
 */
class TeamResRecordTest {

    @Test
    void shouldCreateRecordWithAllFields() {
        TeamRes team = new TeamRes("team-1", "Switzerland", "SUI", "flag-url");
        
        assertEquals("team-1", team.id());
        assertEquals("Switzerland", team.name());
        assertEquals("SUI", team.shortName());
        assertEquals("flag-url", team.flag());
    }

    @Test
    void shouldSupportEqualityBasedOnAllFields() {
        TeamRes team1 = new TeamRes("team-1", "Switzerland", "SUI", "flag-url");
        TeamRes team2 = new TeamRes("team-1", "Switzerland", "SUI", "flag-url");
        TeamRes team3 = new TeamRes("team-2", "Germany", "GER", "flag-url-2");
        
        assertEquals(team1, team2);
        assertNotEquals(team1, team3);
        assertEquals(team1.hashCode(), team2.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        TeamRes team = new TeamRes(null, null, null, null);
        
        assertNull(team.id());
        assertNull(team.name());
        assertNull(team.shortName());
        assertNull(team.flag());
    }

    @Test
    void shouldGenerateToString() {
        TeamRes team = new TeamRes("team-1", "Switzerland", "SUI", "flag-url");
        
        String toString = team.toString();
        assertTrue(toString.contains("team-1"));
        assertTrue(toString.contains("Switzerland"));
    }
}
