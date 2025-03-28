package comp3350.gymbuddy.logic.exception;

/**
 * Base exception for all validation errors and invalid inputs
 */
public class InvalidInputException extends BusinessException {
    public InvalidInputException(String message) {
        super(message);
    }
    
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
