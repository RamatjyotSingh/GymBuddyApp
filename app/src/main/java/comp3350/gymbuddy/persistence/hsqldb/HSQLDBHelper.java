package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides connections to HSQLDB with project configurations set.
 */
public class HSQLDBHelper {
    private static final String FILE_PATH = "gymbuddydb";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    // Ensure the JDBCDriver exists.
    static {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load HSQLDB JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:hsqldb:file:" + FILE_PATH + ";shutdown=true";
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}
