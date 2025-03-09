package comp3350.gymbuddy.tests.integration;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import java.io.File;

import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;

/**
 * Base class for all database integration tests.
 * Handles setup and teardown of test database.
 */
public class DBIntegrationTestHelper {
    protected Context context;
    
    @Before
    public void setUp() {
        // Get Android context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        // Set up paths for test database and SQL scripts
        String dbPath = new File(context.getFilesDir(), "test_db").getAbsolutePath();
        String sqlScriptPath = new File(context.getFilesDir(), "sql_scripts").getAbsolutePath();
        
        // Configure HSQLDBHelper for testing
        HSQLDBHelper.setDatabaseDirectoryPath(dbPath);
        HSQLDBHelper.setSqlScriptDirectory(sqlScriptPath);
        HSQLDBHelper.setTestMode(true);
        
        // Ensure clean test database for each test
        HSQLDBHelper.resetTestDatabase();
    }
    
    @After
    public void tearDown() {
        HSQLDBHelper.resetTestDatabase();
    }
}