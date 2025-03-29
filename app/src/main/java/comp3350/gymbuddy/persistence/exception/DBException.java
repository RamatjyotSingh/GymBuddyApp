package comp3350.gymbuddy.persistence.exception;

/**
 * DBException is a custom exception class for handling database-related errors.
 * It extends RuntimeException to allow unchecked exceptions in the application.
 */
public class DBException extends RuntimeException {
    /**
     * Constructs a new DBException with the specified error message.
     * @param message The detailed error message explaining the cause of the exception.
     */
    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
