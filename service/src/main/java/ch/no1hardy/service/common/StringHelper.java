package ch.no1hardy.service.common;

import jakarta.validation.constraints.NotNull;

public abstract class StringHelper {
    /**
     * Formats a user ID for display purposes. Usually this means returning the last 7 characters of the ID
     * @param id the user ID to format, typically a UUID or similar identifier.
     * @return a formatted string representation of the ID, suitable for display.
     */
    public static String getFormattedId(@NotNull String id) {
        return id.length() > 7 ? id.substring(id.length() - 7) : id;
    }
}
