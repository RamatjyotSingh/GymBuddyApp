package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when workout session-related operations fail
 */
public class WorkoutSessionAccessException extends DataAccessException {
    public WorkoutSessionAccessException(String message) {
        super(message);
    }
    
    public WorkoutSessionAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}