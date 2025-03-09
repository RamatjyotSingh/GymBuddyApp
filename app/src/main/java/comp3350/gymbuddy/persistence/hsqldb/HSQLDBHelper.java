package comp3350.gymbuddy.persistence.hsqldb;

import android.content.Context;
import android.util.Log;

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
    private static final String TAG = "HSQLDBHelper";
    private static final String PROD_FILE_PATH = "gymbuddydb";
    private static final String TEST_FILE_PATH = "test_gymbuddydb"; // Separate test DB
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    private static boolean initialized = false;
    private static boolean isTestMode = false;

    /**
     * Enables or disables test mode.
     * @param testMode True to use the test database, false for production.
     */
    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
        initialized = false; // Reset initialization state
    }

    /**
     * Runs an SQL script from the assets folder.
     */
    private static void runScript(Context context, String filepath) throws DBException {
        Log.d(TAG, "Running SQL script: " + filepath);

        try (Connection conn = getConnectionDriver(context);
             Statement stmt = conn.createStatement();
             InputStream is = context.getAssets().open(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            for (String statement : sql.toString().split(";")) {
                if (!statement.trim().isEmpty()) {
                    try {
                        stmt.execute(statement.trim());
                    } catch (SQLException sqlException) {
                        Log.e(TAG, "SQL Execution Error: " + sqlException.getMessage());
                        throw new DBException("SQL Error in script '" + filepath + "': " + sqlException.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading script file: " + filepath, e);
            throw new DBException("Failed to read SQL script '" + filepath + "'.");
        } catch (SQLException e) {
            Log.e(TAG, "SQL Execution Error in script: " + filepath, e);
            throw new DBException("Failed to execute SQL script '" + filepath + "'.");
        }
    }

    /**
     * Initializes the database by creating tables and inserting default data.
     */
    private static void initializeDatabase(Context context) throws DBException {
        if (!initialized) {
            Log.d(TAG, "Initializing database...");
            try {
                runScript(context, "create_tables.sql");
                runScript(context, "insert_data.sql");
                initialized = true;
                Log.d(TAG, "Database initialized successfully.");
            } catch (DBException e) {
                Log.e(TAG, "Database initialization failed.", e);
                throw new DBException("Failed to initialize database: " + e.getMessage());
            }
        }
    }

    /**
     * Provides a connection to the database, initializing it if necessary.
     */
    public static Connection getConnection() throws SQLException {
        Context context = GlobalApplication.getAppContext();

        if (!initialized) {
            try {
                initializeDatabase(context);
            } catch (DBException e) {
                throw new SQLException("Database initialization error: " + e.getMessage());
            }
        }

        return getConnectionDriver(context);
    }

    /**
     * Establishes a connection to the appropriate database file.
     */
    private static Connection getConnectionDriver(Context context) throws SQLException {
        String dbFilePath = isTestMode ? TEST_FILE_PATH : PROD_FILE_PATH;
        File dbFile = new File(context.getFilesDir(), dbFilePath);
        String dbPath = dbFile.getAbsolutePath();

        String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";
        Log.d(TAG, "Connecting to HSQLDB: " + url);

        try {
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            Log.e(TAG, "Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Resets the test database to a clean state.
     */
    public static void resetTestDatabase(Context context) {
        Log.d(TAG, "Resetting test database...");

        File testDbFile = new File(context.getFilesDir(), TEST_FILE_PATH);
        if (testDbFile.exists()) {
            if (!testDbFile.delete()) {
                Log.e(TAG, "Failed to delete test database file.");
                throw new RuntimeException("Failed to delete test database file.");
            }
        }

        initialized = false;
        try {
            Log.d(TAG, "Reinitializing test database...");
            initializeDatabase(context);
            Log.d(TAG, "Test database reset and reinitialized.");
        } catch (DBException e) {
            throw new RuntimeException("Test database failed to initialize: " + e.getMessage());
        }
    }

}
