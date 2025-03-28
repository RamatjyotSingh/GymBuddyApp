package comp3350.gymbuddy.presentation.util;

import timber.log.Timber;

/**
 * Utility class for handling exceptions in the UI layer
 */
public class ErrorHandler {
    private final ErrorDisplay errorDisplay;
    private static final String DEFAULT_ERROR_MESSAGE =
            "An unexpected error occurred. Please contact support.";
    
    public ErrorHandler(ErrorDisplay errorDisplay) {
        this.errorDisplay = errorDisplay;
    }


    /**
     * Handle any exception and show appropriate user message
     */
    public void handle(Exception e) {
        String message = extractMessage(e);
        errorDisplay.showError(message);
        Timber.e(e, "An error occurred: %s", message);
    }
    public void handle(Exception e, String displayMessage) {
        String message = extractMessage(e);
        errorDisplay.showError(displayMessage);
        Timber.e(e, "An error occurred: %s", message);
    }
    
    /**
     * Extract a meaningful message from the exception
     */
    private String extractMessage(Exception e) {
        if (e == null) {
            return DEFAULT_ERROR_MESSAGE;
        }
        
        String message = e.getMessage();
        return (message != null && !message.trim().isEmpty()) 
                ? message 
                : DEFAULT_ERROR_MESSAGE;
    }

    public String getDefaultErrorMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }
}