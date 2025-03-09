package comp3350.gymbuddy.persistence.hsqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import comp3350.gymbuddy.persistence.exception.DBException;
import timber.log.Timber;

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

    private static String dbDirectoryPath;
    private static String sqlScriptDirectory;

    public static void setDatabaseDirectoryPath(String path) {
        dbDirectoryPath = path;
    }

    public static void setSqlScriptDirectory(String path) {
        sqlScriptDirectory = path;
    }

    /**
     * Enables or disables test mode.
     * @param testMode True to use the test database, false for production.
     */
    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
        initialized = false; // Reset initialization state
    }

    /**
     * Runs an SQL script from the file system.
     */
    private static void runScript(String filename) throws DBException {
        if (sqlScriptDirectory == null) {
            throw new DBException("SQL script directory not set. Call setSqlScriptDirectory() first.");
        }

        File sqlFile = new File(sqlScriptDirectory, filename);

        try (Connection conn = getConnectionDriver();
             Statement stmt = conn.createStatement();
             FileInputStream fis = new FileInputStream(sqlFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            for (String statement : sql.toString().split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement.trim());
                }
            }
        } catch (Exception e) {
            throw new DBException("Error running SQL script: " + e.getMessage());
        }
    }

    /**
     * Initializes the database by creating tables and inserting default data.
     */
    private static void initializeDatabase() throws DBException {
        if (!initialized) {
            Timber.tag(TAG).d("Initializing database...");
            try {
                runScript("create_tables.sql");
                runScript("insert_data.sql");
                initialized = true;
                Timber.tag(TAG).d("Database initialized successfully.");
            } catch (DBException e) {
                Timber.tag(TAG).e(e, "Database initialization failed.");
                throw new DBException("Failed to initialize database: " + e.getMessage());
            }
        }
    }

    /**
     * Provides a connection to the database, initializing it if necessary.
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            try {
                initializeDatabase();
            } catch (DBException e) {
                throw new SQLException("Database initialization error: " + e.getMessage());
            }
        }

        return getConnectionDriver();
    }

    /**
     * Establishes a connection to the appropriate database file.
     */
    private static Connection getConnectionDriver() throws SQLException {
        if (dbDirectoryPath == null) {
            throw new SQLException("Database path not set. Call setDatabaseDirectoryPath() first.");
        }

        String dbFilePath = isTestMode ? TEST_FILE_PATH : PROD_FILE_PATH;
        File dbFile = new File(dbDirectoryPath, dbFilePath);
        String dbPath = dbFile.getAbsolutePath();

        String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";
        Timber.tag(TAG).d("Connecting to HSQLDB: %s", url);

        try {
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            Timber.tag(TAG).e("Database connection failed: %s", e.getMessage());
            throw e;
        }
    }

    /**
     * Resets the test database to a clean state.
     */
    public static void resetTestDatabase() {
        Timber.tag(TAG).d("Resetting test database...");

        File testDbFile = new File(dbDirectoryPath, TEST_FILE_PATH);
        if (testDbFile.exists()) {
            if (!testDbFile.delete()) {
                Timber.tag(TAG).e("Failed to delete test database file.");
                throw new RuntimeException("Failed to delete test database file.");
            }
        }

        initialized = false;
        try {
            Timber.tag(TAG).d("Reinitializing test database...");
            initializeDatabase();
            Timber.tag(TAG).d("Test database reset and reinitialized.");
        } catch (DBException e) {
            throw new RuntimeException("Test database failed to initialize: " + e.getMessage());
        }
    }

}
