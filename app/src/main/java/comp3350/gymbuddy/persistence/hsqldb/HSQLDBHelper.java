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

    // Whether the database has been initialized on this run yet.
    private static boolean initialized = false;

    private static void runScript(Context context, String filepath) throws DBException {
        // Get a connection and open the file.
        String linebad = "";
        try (Connection conn = getConnectionDriver(context);
             Statement stmt = conn.createStatement();
             InputStream is = context.getAssets().open(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            // Read in the file.
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Split SQL statements (assuming they end with semicolons)
            for (String statement : sql.toString().split(";")) {
                if (!statement.trim().isEmpty()) {
                    linebad = statement;
                    stmt.execute(statement.trim());
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println(linebad);
            throw new DBException("Failed to run script '" + filepath + "'.");
        }
    }

    private static void initializeDatabase(Context context) throws DBException {
        if (!initialized) {
            try {
                // Create SQL tables.
                runScript(context, "create_tables.sql");

                // Insert data.
                runScript(context, "insert_data.sql");

                initialized = true;
            } catch (DBException e) {
                throw new DBException("Failed to initialize database.");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        // Get the application context.
        Context context = GlobalApplication.getAppContext();

        // Initialize if needed.
        if (!initialized) {
            initializeDatabase(context);
        }

        return getConnectionDriver(context);
    }

    private static Connection getConnectionDriver(Context context) throws SQLException {
        // Get an appropriate filepath to the database through the Android API.
        File dbFile = new File(context.getFilesDir(), FILE_PATH);
        String dbPath = dbFile.getAbsolutePath();

        // Establish database connection.
        String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}
