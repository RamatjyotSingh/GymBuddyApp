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
import java.util.Properties;

import comp3350.gymbuddy.persistence.exception.DBException;

/**
 * Provides connections to HSQLDB with project configurations set.
 * Implemented as a singleton to ensure a single connection across the app.
 */
public class HSQLDBHelper implements AutoCloseable {
    // Singleton instance
    private static HSQLDBHelper instance;
    
    // DB configuration
    private String connectionString;
    private String username;
    private String password;
    private Properties dbOptions;
    
    // Connection
    private Connection connection;
    
    // Context reference
    private final Context context;
    
    // Whether the database has been initialized
    private boolean initialized = false;

    // Private constructor for singleton
    private HSQLDBHelper(Context context) {
        // Use application context to avoid memory leaks
        this.context = context.getApplicationContext();
        loadConfiguration();
    }
    
    /**
     * Get singleton instance of HSQLDBHelper
     * @param context The context used to access assets
     * @return The singleton HSQLDBHelper instance
     */
    public static synchronized HSQLDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new HSQLDBHelper(context);
        }
        return instance;
    }
    
    /**
     * Reset the singleton instance (mainly for testing purposes)
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.disconnect();
            instance = null;
        }
    }
    
    private void loadConfiguration() {
        try {
            Properties props = new Properties();
            InputStream is = context.getAssets().open("config.properties");
            props.load(is);
            is.close();
            
            // Load connection properties
            connectionString = props.getProperty("db.connection_string", "jdbc:hsqldb:file:data/mydb;shutdown=true");
            username = props.getProperty("db.username", "SA");
            password = props.getProperty("db.password", "");
            
            // Load HSQLDB options
            dbOptions = new Properties();
            for (String key : props.stringPropertyNames()) {
                if (key.startsWith("db.options.hsqldb.")) {
                    String optionName = key.substring("db.options.hsqldb.".length());
                    dbOptions.setProperty(optionName, props.getProperty(key));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public void connect() throws DBException {
        try {
            // Only connect if not already connected
            if (connection == null) {
                // Load HSQLDB JDBC driver
                Class.forName("org.hsqldb.jdbc.JDBCDriver");
                
                // Get appropriate filepath
                File dbFile = new File(context.getFilesDir(), "mydb");
                String dbPath = dbFile.getAbsolutePath();
                
                // Establish connection
                String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";
                connection = DriverManager.getConnection(url, username, password);
                
                // Set database options
                if (dbOptions != null && !dbOptions.isEmpty()) {
                    try (Statement stmt = connection.createStatement()) {
                        for (String key : dbOptions.stringPropertyNames()) {
                            String value = dbOptions.getProperty(key);
                            stmt.execute("SET " + key + " " + value);
                        }
                    }
                }
                
                // Initialize if needed
                if (!initialized) {
                    initializeDatabase();
                }
            }
        } catch (ClassNotFoundException e) {
            throw new DBException("HSQLDB JDBC driver not found"+e.getMessage());
        } catch (SQLException e) {
            throw new DBException("Failed to connect to database"+e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SHUTDOWN");
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to disconnect from database", e);
            }
        }
    }
    
    private void runScript(String filepath) throws DBException {
        try (Statement stmt = connection.createStatement();
             InputStream is = context.getAssets().open(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

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
        } catch (IOException | SQLException e) {
            throw new DBException("Failed to run script '" + filepath + "'."+e.getMessage());
        }
    }

    private void initializeDatabase() throws DBException {
        try {
            // Create SQL tables
            runScript("db/Project.script");

            initialized = true;
        } catch (DBException e) {
            throw new DBException("Failed to initialize database."+e.getMessage());
        }
    }

    /**
     * Static method to get a database connection.
     * @return The database connection
     * @throws DBException if the database is not initialized or connection fails
     */
    public static Connection getConnection() throws DBException {
        if (instance == null) {
            throw new DBException("HSQLDBHelper not initialized. Call getInstance(context) first.");
        }
        if (instance.connection == null) {
            instance.connect();
        }
        return instance.connection;
    }

    @Override
    public void close() {
        disconnect();
    }
}