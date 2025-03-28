package comp3350.gymbuddy.tests.persistence;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.factory.HSQLDBFactory;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;
import timber.log.Timber;

public class HSQLDBTestHelper {
    private static final String TAG = "HSQLDBTestHelper";
    private static final String TEST_DB_NAME = "test_gymbuddydb";
    private static final String SCRIPT_FILE_NAME = "Project.script";
    private static final String CONFIG_FILE_NAME = "hsqldb.properties";
    
    private String scriptPath;
    private String configPath;
    private HSQLDBFactory factory;
    private IDatabase database;
    
    public HSQLDBTestHelper() {
        setupTestFiles();
    }
    
    private void setupTestFiles() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File dbDir = new File(context.getFilesDir(), "testdb");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        
        // Copy script file
        scriptPath = new File(dbDir, SCRIPT_FILE_NAME).getAbsolutePath();
        copyAssetToFile("db/" + SCRIPT_FILE_NAME, scriptPath);
        
        // Create config file
        configPath = new File(dbDir, CONFIG_FILE_NAME).getAbsolutePath();
        createConfigFile(configPath, dbDir.getAbsolutePath());
        
        Timber.tag(TAG).d("Test files set up at %s", dbDir.getAbsolutePath());
    }
    
    private void copyAssetToFile(String assetPath, String destPath) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        try (InputStream is = context.getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(destPath)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            Timber.tag(TAG).d("Copied asset %s to %s", assetPath, destPath);
        } catch (IOException e) {
            Timber.tag(TAG).e(e, "Failed to copy asset %s to %s", assetPath, destPath);
        }
    }
    
    private void createConfigFile(String filePath, String dbDir) {
        String props = 
            "hsqldb.protocol=jdbc:hsqldb:\n" +
            "hsqldb.type=file:\n" +
            "hsqldb.prod.filename=" + TEST_DB_NAME + "\n" +
            "hsqldb.url=jdbc:hsqldb:file:" + dbDir + File.separator + TEST_DB_NAME + "\n" +
            "hsqldb.user=SA\n" +
            "hsqldb.password=\n";
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(props.getBytes());
            Timber.tag(TAG).d("Created config file at %s", filePath);
        } catch (IOException e) {
            Timber.tag(TAG).e(e, "Failed to create config file at %s", filePath);
        }
    }
    
    public void setUp() throws DBException {
        factory = new HSQLDBFactory(scriptPath, configPath);
        database = factory.createDatabase();
        database.initialize();
        
        // Clear existing data before initializing schema
        clearAllTables();
        
        // Initialize the schema
        initializeSchema();
        
        Timber.tag(TAG).d("Test database initialized");
    }
    
    private void clearAllTables() throws DBException {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = database.getConnection();
            stmt = conn.createStatement();
            
            // Delete data in order to preserve foreign key constraints
            stmt.execute("DELETE FROM session_item");
            stmt.execute("DELETE FROM workout_session");
            stmt.execute("DELETE FROM profile_exercise");
            stmt.execute("DELETE FROM exercise_tag");
            stmt.execute("DELETE FROM workout_profile");
            stmt.execute("DELETE FROM tag");
            stmt.execute("DELETE FROM exercise");
            
            conn.commit();
            Timber.tag(TAG).d("All tables cleared");
        } catch (SQLException e) {
            // If tables don't exist, that's fine
            Timber.tag(TAG).d("Tables might not exist yet: %s", e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new DBException("Error closing resources: " + e.getMessage(), e);
            }
        }
    }
    
    private void initializeSchema() throws DBException {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = database.getConnection();
            stmt = conn.createStatement();
            
            // Create tables - EXACTLY matching Project.script
            stmt.execute("CREATE TABLE IF NOT EXISTS exercise (" +
                    "exercise_id INTEGER NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "instructions VARCHAR(1000) NOT NULL, " +
                    "image_path VARCHAR(255), " +
                    "is_time_based BOOLEAN NOT NULL, " +
                    "has_weight BOOLEAN NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS tag (" +
                    "tag_id INTEGER NOT NULL PRIMARY KEY, " +
                    "tag_name VARCHAR(50) NOT NULL, " +
                    "tag_type INTEGER NOT NULL, " +
                    "text_color VARCHAR(7) NOT NULL, " +
                    "background_color VARCHAR(7) NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS exercise_tag (" +
                    "exercise_id INTEGER NOT NULL, " +
                    "tag_id INTEGER NOT NULL, " +
                    "PRIMARY KEY (exercise_id, tag_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS workout_profile (" +
                    "profile_id INTEGER PRIMARY KEY, " +
                    "profile_name VARCHAR(255) NOT NULL, " +
                    "icon_path VARCHAR(255) DEFAULT NULL, " +
                    "is_deleted BIT DEFAULT 0)");

            stmt.execute("CREATE TABLE IF NOT EXISTS profile_exercise (" +
                    "profile_id INTEGER NOT NULL, " +
                    "exercise_id INTEGER NOT NULL, " +
                    "sets INTEGER NOT NULL, " +
                    "reps INTEGER NOT NULL, " +
                    "weight DOUBLE PRECISION DEFAULT 0, " +
                    "duration DOUBLE PRECISION DEFAULT 0, " +
                    "PRIMARY KEY (profile_id, exercise_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS workout_session (" +
                    "session_id INTEGER PRIMARY KEY, " +
                    "start_time BIGINT NOT NULL, " +
                    "end_time BIGINT NOT NULL, " +
                    "profile_id INTEGER)");

            stmt.execute("CREATE TABLE IF NOT EXISTS session_item (" +
                    "session_id INTEGER NOT NULL, " +
                    "exercise_id INTEGER NOT NULL, " +
                    "reps INTEGER DEFAULT 0, " +
                    "weight DOUBLE PRECISION DEFAULT 0, " +
                    "duration INTEGER DEFAULT 0, " +
                    "PRIMARY KEY (session_id, exercise_id))");
            
            // Add sample data 
            insertSampleData(stmt);
            
            conn.commit();
        } catch (SQLException e) {
            throw new DBException("Error initializing test schema: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new DBException("Error closing resources: " + e.getMessage(), e);
            }
        }
    }
    
    private void insertSampleData(Statement stmt) throws SQLException {
        // Insert sample exercises one at a time - matching Project.script structure
        stmt.execute("INSERT INTO exercise (exercise_id, name, instructions, image_path, is_time_based, has_weight) " +
                    "VALUES (1, 'Push-Up', '1. Start in a plank position with hands slightly wider than shoulder-width apart.', 'images/push_up.png', FALSE, FALSE)");
        
        stmt.execute("INSERT INTO exercise (exercise_id, name, instructions, image_path, is_time_based, has_weight) " +
                    "VALUES (2, 'Squat', '1. Stand with feet shoulder-width apart, toes pointed slightly outward.', 'images/squat.png', FALSE, TRUE)");
        
        stmt.execute("INSERT INTO exercise (exercise_id, name, instructions, image_path, is_time_based, has_weight) " +
                    "VALUES (3, 'Plank', '1. Place forearms on the ground with elbows aligned below shoulders.', 'images/plank.png', TRUE, FALSE)");
        
        // Insert sample tags one at a time
        stmt.execute("INSERT INTO tag (tag_id, tag_name, tag_type, text_color, background_color) " +
                    "VALUES (1, 'Upper Body', 1, '#FFFFFF', '#1D4ED8')");
        
        stmt.execute("INSERT INTO tag (tag_id, tag_name, tag_type, text_color, background_color) " +
                    "VALUES (2, 'Lower Body', 1, '#FFFFFF', '#6B21A8')");
        
        stmt.execute("INSERT INTO tag (tag_id, tag_name, tag_type, text_color, background_color) " +
                    "VALUES (3, 'Core', 1, '#FFFFFF', '#0D9488')");
        
        stmt.execute("INSERT INTO tag (tag_id, tag_name, tag_type, text_color, background_color) " +
                    "VALUES (4, 'Beginner', 0, '#FFFFFF', '#15803D')");
        
        // Link tags to exercises one at a time
        stmt.execute("INSERT INTO exercise_tag (exercise_id, tag_id) VALUES (1, 1)");
        
        stmt.execute("INSERT INTO exercise_tag (exercise_id, tag_id) VALUES (1, 4)");
        
        stmt.execute("INSERT INTO exercise_tag (exercise_id, tag_id) VALUES (2, 2)");
        
        stmt.execute("INSERT INTO exercise_tag (exercise_id, tag_id) VALUES (3, 3)");
        
        // Insert sample workout profiles one at a time
        stmt.execute("INSERT INTO workout_profile (profile_id, profile_name, icon_path) " +
                    "VALUES (1, 'Full Body Workout', 'icons/full_body.png')");
        
        stmt.execute("INSERT INTO workout_profile (profile_id, profile_name, icon_path) " +
                    "VALUES (2, 'Upper Body Focus', 'icons/upper_body.png')");
        
        // Link exercises to workout profiles one at a time - include duration
        stmt.execute("INSERT INTO profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) " +
                    "VALUES (1, 1, 3, 10, 0, 0)");
        
        stmt.execute("INSERT INTO profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) " +
                    "VALUES (1, 2, 3, 12, 50, 0)");
        
        stmt.execute("INSERT INTO profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) " +
                    "VALUES (2, 1, 4, 15, 0, 0)");
        
        // Insert sample workout session
        long startTime = System.currentTimeMillis() - 3600000; // 1 hour ago
        long endTime = System.currentTimeMillis();
        
        stmt.execute("INSERT INTO workout_session (session_id, profile_id, start_time, end_time) " +
                    "VALUES (1, 1, " + startTime + ", " + endTime + ")");
        
        // Insert sample session items one at a time
        stmt.execute("INSERT INTO session_item (session_id, exercise_id, reps, weight, duration) " +
                    "VALUES (1, 1, 10, 0, 0)");
        
        stmt.execute("INSERT INTO session_item (session_id, exercise_id, reps, weight, duration) " +
                    "VALUES (1, 2, 12, 45, 0)");
    }
    
    public HSQLDBFactory getFactory() {
        return factory;
    }
    
    public IDatabase getDatabase() {
        return database;
    }
    
    public void tearDown() throws DBException {
        if (database != null) {
            database.shutdown();
        }
        Timber.tag(TAG).d("Test database shut down");
    }
    
    public void resetDatabase() throws DBException {
        if (database != null) {
            clearAllTables();
            initializeSchema();
        }
        Timber.tag(TAG).d("Test database reset");
    }
}