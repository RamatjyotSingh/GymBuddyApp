package comp3350.gymbuddy.presentation.util;

/**
 * Interface for displaying errors to the user
 */
public interface ErrorDisplay {
    /**
     * Display an error message to the user
     * @param message The message to display
     */
    void showError(String message);
}