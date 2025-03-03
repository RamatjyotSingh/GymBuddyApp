package comp3350.gymbuddy.persistence.database;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import comp3350.gymbuddy.application.ConfigManager;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static DatabaseManager instance;
    private  Connection connection;

    // Private constructor to initialize the database connection
    private DatabaseManager(Context context) {
        ConfigManager config = new ConfigManager(context, "db_config.properties");

        // Retrieve database configuration properties
        String url = config.getActiveDbProperty("url");
        String user = config.getActiveDbProperty("user");
        String password = config.getActiveDbProperty("password");

        try {
            // Establish the database connection
            connection = DriverManager.getConnection(url, user, password);
            Log.d(TAG, "Connected to HSQLDB (" + config.getProperty("db.active") + ") database!");
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    // Synchronized method to get the singleton instance of DatabaseManager
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    // Method to get the database connection
    public  Connection getConnection() {
        return connection;
    }

    // Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                instance = null;
                Log.d(TAG, "Database connection closed.");
            }
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }
}