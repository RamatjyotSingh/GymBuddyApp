package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when name value is invalid
 */
public class InvalidNameException extends InvalidInputException {
    public InvalidNameException(String message) {
        super(message);
    }
    
    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
