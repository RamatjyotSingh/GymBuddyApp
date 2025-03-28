package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when weight value is invalid
 */
public class InvalidWeightException extends InvalidInputException {
    public InvalidWeightException(String message) {
        super(message);
    }
    
    public InvalidWeightException(String message, Throwable cause) {
        super(message, cause);
    }
}
