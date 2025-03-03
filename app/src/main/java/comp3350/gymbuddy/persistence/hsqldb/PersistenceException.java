package comp3350.gymbuddy.persistence.hsqldb;

public class PersistenceException extends RuntimeException {

    // Constructor with only a message
    public PersistenceException(String message) {
        super(message);
    }

    // Constructor with a message and a cause (exception chaining)
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that only takes another exception (keeps your version)
    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
