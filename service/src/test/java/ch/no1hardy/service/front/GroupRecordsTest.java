package ch.no1hardy.service.front;

import ch.no1hardy.service.front.group.AvailableJokers;
import ch.no1hardy.service.front.group.CardGroupRes;
import ch.no1hardy.service.front.group.GroupReq;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Group-related records.
 * Verifies that the records behave correctly as immutable data carriers.
 */
class GroupRecordsTest {

    @Test
    void shouldCreateAvailableJokersRecord() {
        AvailableJokers jokers = new AvailableJokers(10, 6);
        
        assertEquals(10, jokers.jDouble());
        assertEquals(6, jokers.jTriple());
    }

    @Test
    void shouldCreateGroupReqRecord() {
        GroupReq req = new GroupReq("Group A", false, 1, "thumbnail.png");
        
        assertEquals("Group A", req.name());
        assertFalse(req.isKnockout());
        assertEquals(1, req.order());
        assertEquals("thumbnail.png", req.thumbnail());
    }

    @Test
    void shouldCreateCardGroupResRecordWithDefaultOrder() {
        CardGroupRes card = new CardGroupRes(
            "group-1", 
            "Group A", 
            85.5, 
            90.0, 
            List.of("flag1", "flag2"), 
            false, 
            null  // null order should default to 0
        );
        
        assertEquals("group-1", card.id());
        assertEquals("Group A", card.name());
        assertEquals(85.5, card.percentage());
        assertEquals(90.0, card.percentageResult());
        assertEquals(2, card.thumbnail().size());
        assertFalse(card.isKnockout());
        assertEquals(0, card.order());  // Should default to 0
    }

    @Test
    void shouldCreateCardGroupResRecordWithSpecifiedOrder() {
        CardGroupRes card = new CardGroupRes(
            "group-1", 
            "Group A", 
            85.5, 
            90.0, 
            List.of("flag1"), 
            true, 
            5
        );
        
        assertEquals(5, card.order());
        assertTrue(card.isKnockout());
    }

    @Test
    void shouldSupportEqualityForAvailableJokers() {
        AvailableJokers jokers1 = new AvailableJokers(10, 6);
        AvailableJokers jokers2 = new AvailableJokers(10, 6);
        AvailableJokers jokers3 = new AvailableJokers(5, 3);
        
        assertEquals(jokers1, jokers2);
        assertNotEquals(jokers1, jokers3);
        assertEquals(jokers1.hashCode(), jokers2.hashCode());
    }
}
