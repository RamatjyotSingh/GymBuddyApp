package comp3350.gymbuddy.persistence.hsqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import comp3350.gymbuddy.persistence.exception.DBException;
import timber.log.Timber;

/**
 * Provides connections to HSQLDB with project configurations set.
 */
public class HSQLDBHelper {

    private static final String TAG = "HSQLDBHelper";

   // Properties keys
    private static final String PROD_FILE_KEY = "db.prod.filename";
    private static final String TEST_FILE_KEY = "db.test.filename";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String SQL_SCRIPTS_KEY = "db.sql.script";

    private static final String  relativeConfigPath = "DBConfig.properties";


    // Default values in case properties can't be loaded
    private static String PROD_FILE_PATH = "gymbuddydb";
    private static String TEST_FILE_PATH = "test_gymbuddydb";
    private static String USER = "SA";
    private static String PASSWORD = "";

    private static boolean initialized = false; // for debugging  output , variables reset after every app restart
    private static boolean propertiesLoaded = false;

    private static String dbDirectoryPath = null;
    private static String SQLScriptPath = "Project.script";





;
    public static void setDatabaseDirectoryPath(String path) {
        dbDirectoryPath = path;
    }


    public static String getAbsSqlScriptPath() {

       return  dbDirectoryPath + File.separator + SQLScriptPath;

    }  
    
    private  static String getAbsConfigPath() {

        return dbDirectoryPath  + File.separator + relativeConfigPath;

    }


    public static void loadConfig() {
        if (!propertiesLoaded) {
            try {
                String configPath = getAbsConfigPath();
                Properties properties = new Properties();
                properties.load(new FileInputStream(configPath));

                PROD_FILE_PATH = properties.getProperty(PROD_FILE_KEY, PROD_FILE_PATH);
                TEST_FILE_PATH = properties.getProperty(TEST_FILE_KEY, TEST_FILE_PATH);
                USER = properties.getProperty(USER_KEY, USER);
                PASSWORD = properties.getProperty(PASSWORD_KEY, PASSWORD);
                SQLScriptPath = properties.getProperty(SQL_SCRIPTS_KEY);

                propertiesLoaded = true;
            } catch (IOException e) {
                Timber.tag(TAG).e("Failed to load database properties: %s", e.getMessage());
            }
        }
    }




  

    /**
     * Runs an SQL script from the file system, executing each statement individually.
     * Optimized to handle multi-value INSERT statements by splitting them into separate executions.
     */
    private static void createDB() throws DBException {

        if (SQLScriptPath == null) {
            throw new DBException("SQL script path not set. Call setAbsSQLPath() first.");
        }
        
        String filename = getAbsSqlScriptPath();
        File sqlFile = new File(filename);
        Timber.tag(TAG).d("Running SQL script: %s", sqlFile.getAbsolutePath());

        try (Connection conn = getConnectionDriver();
             Statement stmt = conn.createStatement();
             FileInputStream fis = new FileInputStream(sqlFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            StringBuilder sqlBuffer = new StringBuilder();
            String line;

            // First pass: Read the entire script to handle multi-line statements
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // Skip empty lines and comments
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                    continue;
                }
                
                sqlBuffer.append(line).append("\n");
            }
            
            // Process the complete SQL script
            String sqlScript = sqlBuffer.toString();
            
            // Split the script at semicolons but keep CREATE TABLE statements intact
            List<String> statements = new ArrayList<>();
            int startPos = 0;
            boolean inString = false;
            boolean multiValueInsert = false;
            String insertPrefix = null;
            
            for (int i = 0; i < sqlScript.length(); i++) {
                char c = sqlScript.charAt(i);
                
                // Handle string literals so we don't mistakenly split at semicolons inside strings
                if (c == '\'') {
                    inString = !inString;
                    continue;
                }
                
                if (inString) {
                    continue; // Skip processing if we're inside a string
                }
                
                // Look for potential multi-value INSERT statements
                if (!multiValueInsert && i + 11 < sqlScript.length() && 
                    sqlScript.substring(i, i + 11).equalsIgnoreCase("INSERT INTO")) {
                    
                    // Find the VALUES keyword
                    int valuesPos = sqlScript.indexOf("VALUES", i);
                    if (valuesPos > 0 && valuesPos < sqlScript.length()) {
                        // Look for a comma followed by opening parenthesis after VALUES
                        int openParenPos = sqlScript.indexOf("(", valuesPos);
                        if (openParenPos > 0) {
                            int closeParenPos = findMatchingCloseParen(sqlScript, openParenPos);
                            
                            if (closeParenPos > 0 && closeParenPos + 1 < sqlScript.length() && 
                                sqlScript.charAt(closeParenPos + 1) == ',') {
                                // This is a multi-value INSERT
                                multiValueInsert = true;
                                insertPrefix = sqlScript.substring(i, valuesPos + 6).trim(); // Include "VALUES"
                                startPos = i; // Mark the start of this statement
                            }
                        }
                    }
                }
                
                // Process statement termination
                if (c == ';') {
                    String statement = sqlScript.substring(startPos, i).trim();
                    
                    if (multiValueInsert) {
                        // Handle multi-value INSERT by splitting into individual statements
                        processMultiValueInsert(statements, statement, insertPrefix);
                        multiValueInsert = false;
                        insertPrefix = null;
                    } else if (!statement.isEmpty()) {
                        // Add normal statement
                        statements.add(statement);
                    }
                    
                    startPos = i + 1;
                }
            }
            
            // Execute each statement individually
            for (String statement : statements) {
                try {
                    if (!statement.trim().isEmpty()) {
                        Timber.tag(TAG).d("Executing SQL: %s", statement);
                        stmt.execute(statement);
                    }
                } catch (SQLException e) {
                    Timber.tag(TAG).e("Error executing statement: %s", statement);
                    Timber.tag(TAG).e("SQLException: %s", e.getMessage());
                    throw e;
                }
            }
        } catch (Exception e) {
            Timber.tag(TAG).e("Error running SQL script %s: %s", filename, e.getMessage());
            throw new DBException("Error running SQL script: " + e.getMessage());
        }
    }

    /**
     * Processes a multi-value INSERT statement by splitting it into individual INSERT statements.
     */
    private static void processMultiValueInsert(List<String> statements, String multiInsert, String insertPrefix) {
        // Find where the values start (after "VALUES")
        int valuesPos = multiInsert.toUpperCase().indexOf("VALUES");
        if (valuesPos < 0) return; // Not a valid INSERT statement
        
        String valuesText = multiInsert.substring(valuesPos + 6).trim();
        
        // Create a pattern for matching value groups respecting nested parentheses
        List<String> valueGroups = getValueGroups(valuesText);

        // Create individual INSERT statements
        for (String valueGroup : valueGroups) {
            String singleInsert = insertPrefix + " " + valueGroup;
            statements.add(singleInsert);
        }
    }

    private static List<String> getValueGroups(String valuesText) {
        List<String> valueGroups = new ArrayList<>();
        int depth = 0;
        int startPos = -1;

        for (int i = 0; i < valuesText.length(); i++) {
            char c = valuesText.charAt(i);

            if (c == '(') {
                depth++;
                if (depth == 1) {
                    startPos = i;
                }
            } else if (c == ')') {
                depth--;
                if (depth == 0 && startPos >= 0) {
                    valueGroups.add(valuesText.substring(startPos, i + 1));
                    startPos = -1;
                }
            }
        }
        return valueGroups;
    }

    /**
     * Finds the matching closing parenthesis for an opening parenthesis at the given position.
     */
    private static int findMatchingCloseParen(String s, int openPos) {
        int depth = 0;
        boolean inString = false;
        
        for (int i = openPos; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (c == '\'' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '(') {
                    depth++;
                } else if (c == ')') {
                    depth--;
                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }
        
        return -1; // No matching parenthesis found
    }

    /**
     * Initializes the database by creating tables and inserting default data.
     */
    public static void init() throws DBException {
        if (!initialized) {
            Timber.tag(TAG).d("Initializing database...");

            try {
                loadConfig();
                createDB();
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
    public static Connection getConnection() throws DBException {

        return getConnectionDriver();
    }

    /**
     * Establishes a connection to the appropriate database file.
     */
    private static Connection getConnectionDriver() throws DBException {
        if (dbDirectoryPath == null) {
            throw new DBException("Database path not set. Call setDatabaseDirectoryPath() first.");
        }

        String dbFilePath = PROD_FILE_PATH;
        File dbFile = new File(dbDirectoryPath, dbFilePath);
        String dbPath = dbFile.getAbsolutePath();

        String url = "jdbc:hsqldb:file:" + dbPath + ";shutdown=true";
        Timber.tag(TAG).d("Connecting to HSQLDB: %s", url);

        try {
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            Timber.tag(TAG).e("Database connection failed: %s", e.getMessage());
            throw new DBException("Failed to connect to database: " + e.getMessage());
        }
    }

    

}
