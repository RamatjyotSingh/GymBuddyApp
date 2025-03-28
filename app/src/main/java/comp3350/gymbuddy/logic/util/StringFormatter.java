package comp3350.gymbuddy.logic.util;

import androidx.annotation.NonNull;

public class StringFormatter {
    private final int SECS_IN_MIN = 60;
    private final int SECS_IN_HOUR = 3600;

    /**
     * Converts a given time in seconds into a human-readable format.
     * The output includes hours, minutes, and seconds as needed.
     * - If the time is less than a minute, only seconds are shown (e.g., "12s").
     * - If the time includes minutes, it shows both minutes and seconds (e.g., "1m 30s").
     * - If the time includes hours, it shows hours, minutes (if nonzero), and seconds (if nonzero)
     *   (e.g., "1h 2s", "1h 15m", "2h 0m 5s").
     *
     * @param timeSec The time in seconds to be formatted.
     * @return A formatted time string with hours (h), minutes (m), and seconds (s).
     */
    public String formatTime(double timeSec) {
        // Round the given time to the nearest whole number of seconds
        int totalSeconds = (int) Math.round(timeSec);

        // Extract hours, minutes, and remaining seconds
        int hours = totalSeconds / SECS_IN_HOUR;
        int minutes = (totalSeconds % SECS_IN_HOUR) / SECS_IN_MIN;
        int seconds = totalSeconds % SECS_IN_MIN;

        return buildTimeString(hours, minutes, seconds);
    }

    /**
     * Converts the given weight into a more presentable format as a String.
     * @param weight the weight to convert
     * @return the formatted weight string
     */
    public String formatWeight(double weight) {
        // Round the given weight.
        int roundedWeight = (int) Math.round(weight);
        return roundedWeight + " lbs";
    }

    /**
     * Builds a string in the format Xh Xm Xs from the provided values.
     * @param hours the number of hours
     * @param minutes the number of minutes
     * @param seconds the number of seconds
     * @return a formatted time string
     */
    @NonNull
    private String buildTimeString(int hours, int minutes, int seconds) {
        StringBuilder result = new StringBuilder();

        // Append hours if nonzero
        if (hours > 0) {
            result.append(hours).append("h ");
        }

        // Append minutes if nonzero or if hours exist (to maintain structure)
        if (minutes > 0 || hours > 0) {
            result.append(minutes).append("m ");
        }

        // Append seconds if nonzero or if no other time unit was added
        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append("s");
        }

        // Trim any trailing space and return the final formatted string
        return result.toString().trim();
    }
}
