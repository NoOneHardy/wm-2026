package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.group.AvailableJokers;
import ch.no1hardy.service.model.group.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GroupMapperTest {
    @Autowired
    private GroupMapper groupMapper;

    @MockitoBean
    public UserHelper userHelper;

    @BeforeEach
    void beforeEach() {
        when(userHelper.getAvailableDoubleJokers(10)).thenReturn(10);
        when(userHelper.getAvailableTripleJokers(6)).thenReturn(6);
    }

    @Test
    void shouldMapJokers() {
        AvailableJokers jokers = groupMapper.toDto(new Group()).getAvailableJokers();
        assertThat(jokers.getJDouble()).isEqualTo(10);
        assertThat(jokers.getJTriple()).isEqualTo(6);
    }
}
