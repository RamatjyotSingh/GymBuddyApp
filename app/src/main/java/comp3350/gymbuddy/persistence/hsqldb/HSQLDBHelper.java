package comp3350.gymbuddy.persistence.hsqldb;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import comp3350.gymbuddy.GlobalApplication;
import comp3350.gymbuddy.persistence.exception.DBException;

/**
 * Provides connections to HSQLDB with project configurations set.
 */
public class HSQLDBHelper {
    // HSQLDB configurations.
    private static final String FILE_PATH = "gymbuddydb";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    // Tracks whether the database has been initialized in the current run.
    private static boolean initialized = false;

    /**
     * Runs an SQL script from the assets folder.
     * @param context The application context.
     * @param filepath The path of the script file inside assets.
     * @throws DBException If an error occurs while executing the script.
     */
    private static void runScript(Context context, String filepath) throws DBException {
        try (Connection conn = getConnectionDriver(context);
             Statement stmt = conn.createStatement();
             InputStream is = context.getAssets().open(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            // Read the script file line by line
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Execute SQL statements (assuming they end with semicolons)
            for (String statement : sql.toString().split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement.trim());
                }
            }
        } catch (IOException | SQLException e) {
            throw new DBException("Failed to run script '" + filepath + "'.");
        }
    }

    /**
     * Initializes the database by creating tables and inserting default data.
     * @param context The application context.
     * @throws DBException If initialization fails.
     */
    private static void initializeDatabase(Context context) throws DBException {
        if (!initialized) {
            try {
                // Execute table creation script.
                runScript(context, "create_tables.sql");

                // Execute data insertion script.
                runScript(context, "insert_data.sql");

                initialized = true;
            } catch (DBException e) {
                throw new DBException("Failed to initialize database.");
            }
        }
    }

    /**
     * Provides a connection to the database, initializing it if necessary.
     * @return A Connection object for HSQLDB.
     * @throws SQLException If a connection error occurs.
     */
    public static Connection getConnection() throws SQLException {
        Context context = GlobalApplication.getAppContext();

        // Ensure database is initialized before establishing a connection.
        if (!initialized) {
            initializeDatabase(context);
        }

        return getConnectionDriver(context);
    }

    /**
     * Establishes a connection to the HSQLDB database file.
     * @param context The application context.
     * @return A Connection object for HSQLDB.
     * @throws SQLException If a connection error occurs.
     */
    private static Connection getConnectionDriver(Context context) throws SQLException {
        // Construct the database file path using the Android file system.
        File dbFile = new File(context.getFilesDir(), FILE_PATH);
        String dbPath = dbFile.getAbsolutePath();

        // Define the JDBC connection URL.
        String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";

        // Return a connection to the database.
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}
