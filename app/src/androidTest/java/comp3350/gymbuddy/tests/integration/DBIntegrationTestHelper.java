package comp3350.gymbuddy.tests.integration;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Arrays;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;
import timber.log.Timber;

/**
 * Base class for all database integration tests.
 * Handles setup and teardown of test database.
 */
public class DBIntegrationTestHelper {
    private static final String TAG = "DBIntegrationTest";
    protected Context context;
    private static boolean databaseInitialized = false;

    // Directory names - separate test DB from production DB
    private static final String TEST_DB_DIR = "test_db";
    private File dbDir;

    @Before
    public void setUp() {
        // Get Android context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Ensure Timber is planted
        if (Timber.forest().isEmpty()) {
            Timber.plant(new Timber.DebugTree());
        }

        // Set up path for test database
        dbDir = new File(context.getFilesDir(), TEST_DB_DIR);
        
        // Create directory if it doesn't exist
        createDirectoryIfNeeded(dbDir);

        // Initialize test database only once per test run
        synchronized (DBIntegrationTestHelper.class) {
            if (!databaseInitialized) {
                // Extract database files
                extractTestFiles();
                databaseInitialized = true;
            }
        }

        // Configure HSQLDBHelper for testing
        HSQLDBHelper.setDatabaseDirectoryPath(dbDir.getAbsolutePath());

        // Reset test database to a clean state
        resetDatabase();
        
        // Verify database is ready for testing
        verifyDatabaseIsReady();
    }

    /**
     * Create a directory if it doesn't exist
     */
    private void createDirectoryIfNeeded(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            Timber.tag(TAG).e("Failed to create directory: %s", dir.getAbsolutePath());
            throw new RuntimeException("Failed to create directory: " + dir.getAbsolutePath());
        }
    }

    /**
     * Extracts all files needed for testing
     */
    private void extractTestFiles() {
        Timber.tag(TAG).d("Extracting test database files");
        
        // Extract database config
        extractFile("db/DBConfig.properties", "DBConfig.properties");
        
        // Extract SQL scripts for database creation and data insertion
        extractFile("db/Project.script", "Project.script");
        
        Timber.tag(TAG).d("Test database files extracted successfully");
    }

    /**
     * Extract a specific file from assets to the destination directory
     */
    private void extractFile(String assetPath, String destFilename) {
        File outputFile = new File(dbDir, destFilename);
        
        // Skip if file already exists and is not empty
        if (outputFile.exists() && outputFile.length() > 0) {
            Timber.tag(TAG).d("File already exists: %s", outputFile.getAbsolutePath());
            return;
        }
        
        try (InputStream is = context.getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            Timber.tag(TAG).d("Extracting file: %s → %s", assetPath, outputFile.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            Timber.tag(TAG).d("Extracted file: %s → %s", assetPath, outputFile.getAbsolutePath());
        } catch (IOException e) {
            Timber.tag(TAG).e("Failed to extract file: %s - %s", assetPath, e.getMessage());
            throw new RuntimeException("Failed to extract file: " + assetPath, e);
        }
    }
    
    /**
     * Resets the test database to a clean state
     */
    private void resetDatabase() {
        try {
            // Ensure any existing connections are closed
            closeConnections();
            
            // Reset the test database by deleting all database files
            cleanTestDatabaseFiles();
            
            // Initialize the database
            HSQLDBHelper.init();
            
            Timber.tag(TAG).d("Test database reset successful");
        } catch (Exception e) {
            Timber.tag(TAG).e("Failed to reset test database: %s", e.getMessage());
            throw new RuntimeException("Failed to reset test database", e);
        }
    }
    
    /**
     * Deletes all test database files in the test directory
     */
    private void cleanTestDatabaseFiles() {
        if (dbDir == null || !dbDir.exists()) {
            Timber.tag(TAG).w("Test database directory doesn't exist");
            return;
        }
        
        // First make sure all connections are closed
        try (Connection conn = HSQLDBHelper.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("SHUTDOWN");
                }
            }
        } catch (Exception e) {
            // Just log and continue
            Timber.tag(TAG).w("Error during database shutdown: %s", e.getMessage());
        }
        
        // Give time for HSQLDB to release file locks
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Delete all HSQLDB database files
        String[] extensions = {".script", ".properties", ".log", ".data", ".backup", ".lobs"};
        String testDbName = "test_gymbuddydb";
        
        Timber.tag(TAG).d("Cleaning test database files in %s", dbDir.getAbsolutePath());
        
        // List all files in the directory
        File[] files = dbDir.listFiles();
        if (files == null) {
            Timber.tag(TAG).w("Failed to list files in test directory");
            return;
        }
        
        // Log all files in directory for debugging
        Timber.tag(TAG).d("Files in test directory: %s", Arrays.toString(files));
        
        // Delete each database file
        for (String ext : extensions) {
            File dbFile = new File(dbDir, testDbName + ext);
            if (dbFile.exists()) {
                // Try several times in case of file locks
                boolean deleted = false;
                for (int attempt = 0; attempt < 3 && !deleted; attempt++) {
                    if (attempt > 0) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    
                    deleted = dbFile.delete();
                }
                
                if (deleted) {
                    Timber.tag(TAG).d("Deleted database file: %s", dbFile.getName());
                } else {
                    Timber.tag(TAG).e("Failed to delete database file: %s", dbFile.getName());
                }
            }
        }
        
        // Preserve config and SQL script files - don't delete them
        // We'll need them for recreating the database
    }
    
    /**
     * Close any open database connections
     */
    private void closeConnections() {
        try (Connection conn = HSQLDBHelper.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                try (Statement stmt = conn.createStatement()) {
                    // Use HSQLDB's SHUTDOWN command for a clean shutdown
                    stmt.execute("SHUTDOWN");
                    Timber.tag(TAG).d("Database connections closed");
                }
            }
        } catch (SQLException | DBException e) {
            // Log but don't fail - this is just cleanup
            Timber.tag(TAG).w("Error during connection cleanup: %s", e.getMessage());
        }
    }

    /**
     * Verify the test database is properly initialized and ready for testing
     */
    private void verifyDatabaseIsReady() {
        try (Connection conn = HSQLDBHelper.getConnection()) {
            if (conn == null || conn.isClosed()) {
                throw new RuntimeException("Database connection is not available after setup");
            }
            
            // Check if tables exist by querying core tables
            for (String table : new String[]{"EXERCISE", "TAG", "WORKOUT_PROFILE"}) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    
                    if (!rs.next()) {
                        throw new RuntimeException("Cannot query " + table + " table - database may not be initialized");
                    }
                    
                    int count = rs.getInt(1);
                    Timber.tag(TAG).d("Database verification: Found %d entries in %s table", count, table);
                }
            }
            
            Timber.tag(TAG).d("Database verification complete: database is ready");
        } catch (SQLException | DBException e) {
            Timber.tag(TAG).e("Database verification failed: %s", e.getMessage());
            throw new RuntimeException("Failed to verify database is ready: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        // Make sure to close connections after each test
        closeConnections();
    }
}