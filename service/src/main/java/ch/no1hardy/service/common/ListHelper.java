package ch.no1hardy.service.common;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ListHelper {
    public static <T> List<T> remove(@NotNull List<T> list, @NotNull T item) {
        List<T> items = new ArrayList<>(list);
        items.remove(item);
        return items;
    }

    public static <T> List<T> add(@NotNull List<T> list, @NotNull T item) {
        List<T> items = new ArrayList<>(list);
        items.add(item);
        return items;
    }
}
