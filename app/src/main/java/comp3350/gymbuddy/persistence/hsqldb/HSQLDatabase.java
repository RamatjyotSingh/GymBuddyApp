package comp3350.gymbuddy.persistence.hsqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import timber.log.Timber;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;

public class HSQLDatabase implements IDatabase {
    private Connection connection;
    private String scriptPath;
    private String configPath;
    private Properties properties;
    private boolean initialized = false;
    private static final String DB_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String TAG = "HSQLDatabase";
    
    public HSQLDatabase() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            Timber.tag(TAG).e("Failed to load HSQLDB driver: %s", e.getMessage());
        }
    }
    
    @Override
    public void setup(String scriptPath, String configPath) {
        this.scriptPath = scriptPath;
        this.configPath = configPath;
        loadProperties();
    }
    
    @Override
    public void initialize() throws DBException {
        if (initialized) {
            Timber.tag(TAG).d("Database already initialized, skipping initialization");
            return;
        }
        
        try {
            // Create database if it doesn't exist
            createConnection();
            
            // Execute initialization script if provided
            if (scriptPath != null && !scriptPath.isEmpty()) {
                File scriptFile = new File(scriptPath);
                if (scriptFile.exists() && scriptFile.canRead()) {
                    Timber.tag(TAG).d("Executing script from: %s", scriptPath);
                    executeScript(scriptPath);
                } else {
                    Timber.tag(TAG).w("Script file does not exist or cannot be read: %s", scriptPath);
                }
            } else {
                Timber.tag(TAG).w("No script path provided for initialization");
            }
            
            initialized = true;
            Timber.tag(TAG).i("Database successfully initialized");
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Failed to initialize database");
            throw new DBException("Failed to initialize database: " + e.getMessage(), e);
        }
    }
    
    // Improved connection creation method with better URL handling
    private void createConnection() throws DBException {
        try {
            if (connection == null || connection.isClosed()) {
                // Get connection properties from the config file
                String protocol = properties.getProperty("hsqldb.protocol", "jdbc:hsqldb:");
                String type = properties.getProperty("hsqldb.type", "file:");
                
                // Get database file path components
                String dbFilePath;
                
                // First try to get the complete URL
                String dbUrl = properties.getProperty("hsqldb.url");
                if (dbUrl != null && !dbUrl.isEmpty()) {
                    Timber.tag(TAG).d("Using direct URL from properties: %s", dbUrl);
                } else {
                    // Construct URL from components if direct URL not provided
                    String filename = properties.getProperty("hsqldb.prod.filename", "gymbuddydb");
                    
                    // Get the directory of the script file to use as base path
                    File scriptFile = new File(scriptPath);
                    String dbDir = scriptFile.getParent();
                    
                    if (dbDir != null && !dbDir.isEmpty()) {
                        dbFilePath = dbDir + File.separator + filename;
                    } else {
                        dbFilePath = filename;
                    }
                    
                    dbUrl = protocol + type + dbFilePath;
                    Timber.tag(TAG).d("Constructed database URL: %s", dbUrl);
                }
                
                // Get authentication details
                String username = properties.getProperty("hsqldb.user", 
                                   properties.getProperty("hsqldb.username", "SA"));
                String password = properties.getProperty("hsqldb.password", "");
                
                Timber.tag(TAG).i("Connecting to database with URL: %s, user: %s", dbUrl, username);
                
                // Create the connection
                connection = DriverManager.getConnection(dbUrl, username, password);
                connection.setAutoCommit(true);
                
                Timber.tag(TAG).i("Database connection established successfully");
            }
        } catch (SQLException e) {
            Timber.tag(TAG).e(e, "Failed to create database connection");
            throw new DBException("Failed to create database connection: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public Connection getConnection() throws DBException {
        if (connection == null) {
            createConnection();
        }
        
        try {
            if (connection.isClosed()) {
                createConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new DBException("Failed to get database connection: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void closeConnection() throws DBException {
        if (connection != null) {
            closeConnection(connection);
            connection = null;
        }
    }
    
    @Override
    public void closeConnection(Connection connection) throws DBException {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DBException("Failed to close database connection: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public Connection beginTransaction() throws DBException {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new DBException("Failed to begin transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void commitTransaction(Connection connection) throws DBException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DBException("Failed to commit transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void rollbackTransaction(Connection connection) throws DBException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DBException("Failed to rollback transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void executeStatement(String sql) throws DBException {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            
            Timber.tag(TAG).d("Executing SQL: %s", sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Timber.tag(TAG).e(e, "Failed to execute SQL: %s", sql);
            throw new DBException("Failed to execute SQL statement: " + e.getMessage(), e);
        } finally {
            // Only close the statement, not the shared connection
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                Timber.tag(TAG).e("Error closing statement: %s", e.getMessage());
            }
        }
    }
    
    @Override
    public void executeScript(String scriptPath) throws DBException {
        Timber.tag(TAG).d("Executing script from: %s", scriptPath);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            Timber.tag(TAG).d("Reading SQL script ");
            StringBuilder sb = new StringBuilder();
            String line;
            int lineCount = 0;
            
            while ((line = reader.readLine()) != null) {
                Timber.tag(TAG).d("Executing SQL script %s",line);

                lineCount++;
                // Skip comments and empty lines
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sb.append(line);
                
                // Execute statement when semicolon is found
                if (line.trim().endsWith(";")) {
                    String sql = sb.toString();
                    try {
                        executeStatement(sql);
                    } catch (DBException e) {
                        Timber.tag(TAG).e("Error at line %d: %s", lineCount, e.getMessage());
                        // Continue execution despite errors
                    }
                    sb.setLength(0); // Clear the buffer
                }
            }
            
            // Execute any remaining SQL that doesn't end with semicolon
            if (sb.length() > 0) {
                String sql = sb.toString();
                if (!sql.trim().isEmpty()) {
                    try {
                        executeStatement(sql);
                    } catch (DBException e) {
                        Timber.tag(TAG).e("Error in final statement: %s", e.getMessage());
                    }
                }
            }
            
            Timber.tag(TAG).d("Script execution completed");
        } catch (IOException e) {
            Timber.tag(TAG).e(e, "Failed to read SQL script");
            throw new DBException("Failed to read SQL script: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void performMaintenance() throws DBException {
        try {
            // HSQLDB maintenance commands
            executeStatement("CHECKPOINT DEFRAG;");
            executeStatement("ANALYZE;");
        } catch (Exception e) {
            throw new DBException("Failed to perform database maintenance: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Properties getProperties() {
        return properties;
    }
    
    @Override
    public void cleanUp() {
        try {
            closeConnection();
        } catch (DBException e) {
            Timber.tag(TAG).e("Error during database cleanup: %s", e.getMessage());
        }
    }
    
    @Override
    public void shutdown() throws DBException {
        try {
            // Special HSQLDB shutdown command
            if (connection != null && !connection.isClosed()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SHUTDOWN;");
                }
            }
            cleanUp();
            initialized = false;
            Timber.tag(TAG).i("Database shutdown completed");
        } catch (Exception e) {
            throw new DBException("Failed to shutdown database: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void reset() throws DBException {
        try {
            // Close existing connection
            closeConnection();
            
            // Re-initialize the database
            initialize();
            Timber.tag(TAG).i("Database reset completed");
        } catch (Exception e) {
            throw new DBException("Failed to reset database: " + e.getMessage(), e);
        }
    }
    
    private void loadProperties() {
        properties = new Properties();
        
        if (configPath == null || configPath.isEmpty()) {
            Timber.tag(TAG).w("No config path provided, using default properties");
            setDefaultProperties();
            return;
        }
        
        File configFile = new File(configPath);
        if (!configFile.exists() || !configFile.canRead()) {
            Timber.tag(TAG).w("Config file does not exist or cannot be read: %s", configPath);
            setDefaultProperties();
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            Timber.tag(TAG).i("Loaded %d properties from %s", properties.size(), configPath);
            
            // Debug - list all loaded properties
            for (String key : properties.stringPropertyNames()) {
                Timber.tag(TAG).d("Property: %s = %s", key, properties.getProperty(key));
            }
        } catch (IOException e) {
            Timber.tag(TAG).e(e, "Failed to load database properties");
            setDefaultProperties();
        }
    }
    
    private void setDefaultProperties() {
        properties = new Properties();
        properties.setProperty("hsqldb.protocol", "jdbc:hsqldb:");
        properties.setProperty("hsqldb.type", "file:");
        properties.setProperty("hsqldb.prod.filename", "gymbuddydb");
        properties.setProperty("hsqldb.url", "jdbc:hsqldb:file:gymbuddy");
        properties.setProperty("hsqldb.user", "SA");
        properties.setProperty("hsqldb.password", "");
        Timber.tag(TAG).i("Set default properties");
    }
}