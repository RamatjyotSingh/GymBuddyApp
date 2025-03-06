package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides connections to HSQLDB with project configurations set.
 */
public class HSQLDBHelper {
    private static final String URL = "jdbc:hsqldb:mem:gymbuddydb";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
