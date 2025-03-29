package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when time value is invalid
 */
public class InvalidTimeException extends InvalidInputException {
    public InvalidTimeException(String message) {
        super(message);
    }
}
