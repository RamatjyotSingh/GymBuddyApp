package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when workout-related operations fail
 */
public class WorkoutAccessException extends DataAccessException {
    public WorkoutAccessException(String message) {
        super(message);
    }
    
    public WorkoutAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}