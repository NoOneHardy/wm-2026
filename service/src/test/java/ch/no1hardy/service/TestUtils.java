package ch.no1hardy.service;

import ch.no1hardy.service.common.DateHelper;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TestUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static CheckTimeResult checkTime(Runnable callBack, Supplier<LocalDateTime> checkDate) {
        return checkTime(callBack, checkDate, true);
    }

    public static <T> void checkTime(Supplier<T> callBack, Function<T, LocalDateTime> checkDate) {
        LocalDateTime before = LocalDateTime.now();
        T result = callBack.get();
        LocalDateTime after = LocalDateTime.now();

        CheckTimeResult checkTimeResult = new CheckTimeResult(before, after);
        checkTimeByResult(checkDate.apply(result), checkTimeResult, true);
    }

    /**
     * Check if the given date is between the time before and after the callback execution.
     * @param callBack some code that modifies the checkDate
     * @param checkDate a supplier that provides the date to check
     * @param expectedResult the expected result of the check
     * @return the time before and after the callback execution
     */
    public static CheckTimeResult checkTime(Runnable callBack, Supplier<LocalDateTime> checkDate, boolean expectedResult) {
        LocalDateTime before = LocalDateTime.now();
        callBack.run();
        LocalDateTime after = LocalDateTime.now();

        CheckTimeResult result = new CheckTimeResult(before, after);
        checkTimeByResult(checkDate.get(), result, expectedResult);
        return result;
    }

    public static void checkTimeByResult(LocalDateTime date, CheckTimeResult result) {
        checkTimeByResult(date, result, true);
    }

    public static void checkTimeByResult(LocalDateTime date, CheckTimeResult result, boolean expectedResult) {
        assertEquals(expectedResult, DateHelper.isBetween(date, result.before(), result.after()));
    }

    public record CheckTimeResult(LocalDateTime before, LocalDateTime after) {
    }
}
