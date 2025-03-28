package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when application initialization fails
 */
public class ApplicationInitException extends BusinessException {
    
    public ApplicationInitException(String message) {
        super(message);
    }
    
    public ApplicationInitException(String message, Throwable cause) {
        super(message, cause);
    }
}