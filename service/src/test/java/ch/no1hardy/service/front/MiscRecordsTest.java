package ch.no1hardy.service.front;

import ch.no1hardy.service.front.leaderboard.RankingRes;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.LoginReq;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for miscellaneous records.
 * Verifies that the records behave correctly as immutable data carriers.
 */
class MiscRecordsTest {

    @Test
    void shouldCreateLoginReqRecord() {
        LoginReq login = new LoginReq("testuser", "password123");
        
        assertEquals("testuser", login.username());
        assertEquals("password123", login.password());
    }

    @Test
    void shouldCreateCheckResRecord() {
        CheckRes check = new CheckRes(true, false);
        
        assertTrue(check.isUsernameAvailable());
        assertFalse(check.isEmailAvailable());
    }

    @Test
    void shouldCreateRankingResRecord() {
        RankingRes ranking = new RankingRes(
            "user-1",
            "testuser",
            "avatar.png",
            150,
            8,
            5
        );
        
        assertEquals("user-1", ranking.id());
        assertEquals("testuser", ranking.username());
        assertEquals("avatar.png", ranking.avatar());
        assertEquals(150, ranking.points());
        assertEquals(8, ranking.prevRanking());
        assertEquals(5, ranking.ranking());
    }

    @Test
    void shouldSupportEqualityForLoginReq() {
        LoginReq login1 = new LoginReq("user", "pass");
        LoginReq login2 = new LoginReq("user", "pass");
        LoginReq login3 = new LoginReq("other", "pass");
        
        assertEquals(login1, login2);
        assertNotEquals(login1, login3);
        assertEquals(login1.hashCode(), login2.hashCode());
    }

    @Test
    void shouldSupportEqualityForCheckRes() {
        CheckRes check1 = new CheckRes(true, true);
        CheckRes check2 = new CheckRes(true, true);
        CheckRes check3 = new CheckRes(false, true);
        
        assertEquals(check1, check2);
        assertNotEquals(check1, check3);
    }

    @Test
    void shouldSupportEqualityForRankingRes() {
        RankingRes rank1 = new RankingRes("id", "user", "av", 100, 5, 3);
        RankingRes rank2 = new RankingRes("id", "user", "av", 100, 5, 3);
        RankingRes rank3 = new RankingRes("id2", "user", "av", 100, 5, 3);
        
        assertEquals(rank1, rank2);
        assertNotEquals(rank1, rank3);
        assertEquals(rank1.hashCode(), rank2.hashCode());
    }
}
