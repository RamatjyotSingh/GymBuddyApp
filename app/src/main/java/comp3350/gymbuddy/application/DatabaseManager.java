//This class sets up the database and executes the scripts

package comp3350.gymbuddy.application;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_USER = "SA";
    private static final String DB_PASSWORD = "";
    private static Connection connection;
    private static Context appContext; // store application context

    // Call this once, e.g., in your Application class or initial Activity
    public static void init(Context context) {
        // Use the application context to avoid memory leaks
        appContext = context.getApplicationContext();
        initDatabase();
    }

    private static void initDatabase() {
        try {
            // Use Android's internal storage directory for the database
            String dbPath = "jdbc:hsqldb:file:"
                    + appContext.getDatabasePath("gymbuddydb").getAbsolutePath()
                    + ";shutdown=true";
            connection = DriverManager.getConnection(dbPath, DB_USER, DB_PASSWORD);
            executeSQLFile("assets/create_tables.sql");
            executeSQLFile("assets/insert_data.sql");
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database: " + e.getMessage(), e);
        }
    }

    private static void executeSQLFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                DatabaseManager.class.getClassLoader().getResourceAsStream(fileName)));
             Statement stmt = connection.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error executing SQL file: " + fileName, e);
        }
    }

    // Now getConnection does not require passing the context
    public static Connection getConnection() {
        if (connection == null) {
            if(appContext == null) {
                throw new RuntimeException("DatabaseManager is not initialized. Call init() first.");
            }
            initDatabase();
        }
        return connection;
    }
}
