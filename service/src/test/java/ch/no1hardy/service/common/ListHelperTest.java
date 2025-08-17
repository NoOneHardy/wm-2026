package ch.no1hardy.service.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ListHelperTest {
    @Test
    @DisplayName("remove(List<T>, T) - should return new list")
    void shouldReturnNewListRemove() {
        List<Integer> origin = List.of(1, 2, 3);
        int originHash = origin.hashCode();

        List<Integer> newList = ListHelper.remove(origin, 2);
        assertNotEquals(originHash, newList.hashCode());
    }

    @Test
    @DisplayName("remove(List<T>, T) - should remove an item")
    void shouldRemoveAnItemRemove() {
        List<Integer> origin = List.of(1, 2, 3);
        assertEquals(3, origin.size());

        List<Integer> newList = ListHelper.remove(origin, 2);
        assertEquals(2, newList.size());
        assertEquals(List.of(1, 3), newList);
    }

    @Test
    @DisplayName("add(List<T>, T) - should return new list")
    void shouldReturnNewListAdd() {
        List<Integer> origin = List.of(1, 2, 3);
        int originHash = origin.hashCode();

        List<Integer> newList = ListHelper.add(origin, 4);
        assertNotEquals(originHash, newList.hashCode());
    }

    @Test
    @DisplayName("add(List<T>, T) - should remove an item")
    void shouldRemoveAnItemAdd() {
        List<Integer> origin = List.of(1, 2, 3);
        assertEquals(3, origin.size());

        List<Integer> newList = ListHelper.add(origin, 4);
        assertEquals(4, newList.size());
        assertEquals(List.of(1, 2, 3, 4), newList);
    }
}
