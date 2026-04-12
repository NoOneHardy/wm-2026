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

    public static <T> void checkTime(Supplier<T> callBack, Function<T, LocalDateTime> checkDate, boolean expectedResult, int offsetMinutes) {
        LocalDateTime before = LocalDateTime.now();
        T result = callBack.get();
        LocalDateTime after = LocalDateTime.now();

        CheckTimeResult checkTimeResult = new CheckTimeResult(before, after);
        checkTimeByResult(checkDate.apply(result), checkTimeResult, expectedResult, offsetMinutes);
    }

    public static CheckTimeResult checkTime(Runnable callBack, Supplier<LocalDateTime> checkDate) {
        return checkTime(callBack, checkDate, true);
    }

    /**
     * Check if the given date is between the time before and after the callback execution.
     *
     * @param callBack       some code that modifies the checkDate
     * @param checkDate      a supplier that provides the date to check
     * @param expectedResult the expected result of the check
     * @return the time before and after the callback execution
     */
    public static CheckTimeResult checkTime(Runnable callBack, Supplier<LocalDateTime> checkDate, boolean expectedResult) {
        return checkTime(callBack, checkDate, expectedResult, 0);
    }

    /**
     * Check if the given date is between the time before and after the callback execution.
     *
     * @param callBack       some code that modifies the checkDate
     * @param checkDate      a supplier that provides the date to check
     * @param expectedResult the expected result of the check
     * @param offsetMinutes  offset in minutes to adjust the check range
     * @return the time before and after the callback execution
     */
    public static CheckTimeResult checkTime(Runnable callBack, Supplier<LocalDateTime> checkDate, boolean expectedResult, int offsetMinutes) {
        LocalDateTime before = LocalDateTime.now();
        callBack.run();
        LocalDateTime after = LocalDateTime.now();

        CheckTimeResult result = new CheckTimeResult(before, after);
        checkTimeByResult(checkDate.get(), result, expectedResult, offsetMinutes);
        return result;
    }

    public static void checkTimeByResult(LocalDateTime date, CheckTimeResult result) {
        checkTimeByResult(date, result, true);
    }

    public static void checkTimeByResult(LocalDateTime date, CheckTimeResult result, boolean expectedResult) {
        checkTimeByResult(date, result, expectedResult, 0);
    }

    public static void checkTimeByResult(LocalDateTime date, CheckTimeResult result, boolean expectedResult, int offsetMinutes) {
        assertEquals(expectedResult, DateHelper.isBetween(
                date,
                result.before().plusMinutes(offsetMinutes),
                result.after().plusMinutes(offsetMinutes)
        ));
    }

    public record CheckTimeResult(LocalDateTime before, LocalDateTime after) {
    }
}
