package comp3350.gymbuddy.application;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import comp3350.gymbuddy.persistence.hsqldb.PersistenceException;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static Connection connection;
    private static Context appContext; // Store application context

    // Call this once- in the initial Activity
    public static void init(Context context) {
        // Use the application context to avoid memory leaks
        appContext = context.getApplicationContext();
        initDatabase();
    }

    private static void initDatabase() {
        try {
            // Load database configuration
            ConfigManager config = new ConfigManager(appContext, "db_config.properties");
            String dbUrl = config.getActiveDbProperty("url");
            String dbUser = config.getActiveDbProperty("user");
            String dbPassword = config.getActiveDbProperty("password");

            if (dbUrl.startsWith("jdbc:hsqldb:file:")) {
                dbUrl = "jdbc:hsqldb:file:" + appContext.getDatabasePath("gymbuddydb").getAbsolutePath() + ";shutdown=true";
            }

            // Establish database connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Execute SQL scripts
            executeSQLFile("create_tables.sql");
            executeSQLFile("insert_data.sql");
        } catch (final SQLException e) {
            throw new PersistenceException("Error initializing database: ", e);
        }
    }

    private static void executeSQLFile(String fileName) {
        try {
            AssetManager assetManager = appContext.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Statement stmt = connection.createStatement();

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0); // Reset for next query
                }
            }

            reader.close();
            stmt.close();
            Log.d(TAG, "Executed SQL file: " + fileName);
        } catch (final Exception e) {
            throw new PersistenceException("Error executing SQL file: ", e);
        }
    }


    // Now getConnection does not require passing the context
    public static Connection getConnection() {
        if (connection == null) {
            if (appContext == null) {
                throw new PersistenceException("DatabaseManager is not initialized. Call init() first.");
            }
            initDatabase();
        }
        return connection;
    }
}
