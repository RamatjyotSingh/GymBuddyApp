package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when exercise-related operations fail
 */
public class ExerciseAccessException extends DataAccessException {
    public ExerciseAccessException(String message) {
        super(message);
    }
    
    public ExerciseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}