package comp3350.gymbuddy.logic.exception;

/**
 * Exception thrown when data access operations fail
 */
public class DataAccessException extends BusinessException {
    public DataAccessException(String message) {
        super(message);
    }
    
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}