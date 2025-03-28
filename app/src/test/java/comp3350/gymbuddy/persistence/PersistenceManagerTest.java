package comp3350.gymbuddy.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import comp3350.gymbuddy.logic.exception.DataAccessException;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceManagerTest {
    
    private static final String TEST_SCRIPT_PATH = "test/testDb.script";
    private static final String TEST_CONFIG_PATH = "test/testDb.properties";
    private PersistenceManager persistenceManager;
    
    @Before
    public void setUp() {
        try {
            // Reset the singleton between tests
            PersistenceManager.getInstance().shutdown();
        } catch (Exception e) {
            // Ignore errors during setup
        }
        persistenceManager = PersistenceManager.getInstance();
    }
    
    @After
    public void tearDown() {
        try {
            persistenceManager.close();
        } catch (Exception e) {
            // Ignore errors during teardown
        }
    }
    
    @Test
    public void testGetInstance() {
        // Test that getInstance returns a non-null instance
        assertNotNull(persistenceManager);
        
        // Test that getInstance returns the same instance
        PersistenceManager anotherInstance = PersistenceManager.getInstance();
        assertEquals(persistenceManager, anotherInstance);
    }
    
    @Test
    public void testInitializeTestMode() throws DataAccessException {
        // Test initialization with test mode (using stub databases)
        persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, false);
        assertTrue(persistenceManager.isInitialized());
        
        // Verify we can get database interfaces
        assertNotNull(persistenceManager.getExerciseDB());
        assertNotNull(persistenceManager.getWorkoutDB());
        assertNotNull(persistenceManager.getWorkoutSessionDB());
    }
    
    @Test
    public void testInitializeWithExistingDB() throws DataAccessException {
        // Test initialization with existing DB flag set to true
        persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, true);
        assertTrue(persistenceManager.isInitialized());
        
        // Verify we can get database interfaces
        assertNotNull(persistenceManager.getExerciseDB());
        assertNotNull(persistenceManager.getWorkoutDB());
        assertNotNull(persistenceManager.getWorkoutSessionDB());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetExerciseDBBeforeInitialization() {
        // Reset to ensure we're not initialized
        persistenceManager.shutdown();
        
        // Should throw IllegalStateException if not initialized
        persistenceManager.getExerciseDB();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWorkoutDBBeforeInitialization() {
        // Reset to ensure we're not initialized
        persistenceManager.shutdown();
        
        // Should throw IllegalStateException if not initialized
        persistenceManager.getWorkoutDB();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWorkoutSessionDBBeforeInitialization() {
        // Reset to ensure we're not initialized
        persistenceManager.shutdown();
        
        // Should throw IllegalStateException if not initialized
        persistenceManager.getWorkoutSessionDB();
    }
    
    @Test
    public void testClose() throws Exception {
        // Initialize first
        persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, false);
        assertTrue(persistenceManager.isInitialized());
        
        // Then close
        persistenceManager.close();
        
        // Should be reset to not initialized
        assertFalse(persistenceManager.isInitialized());
    }
    
    @Test
    public void testReset() throws DataAccessException {
        // Initialize first
        persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, false);
        assertTrue(persistenceManager.isInitialized());
        
        // Reset
        persistenceManager.reset();
        
        // Reset should nullify the database references but not change initialization status
        // Let's test that we can still get managers (they'll be recreated)
        assertNotNull(persistenceManager.getExerciseDB());
        assertNotNull(persistenceManager.getWorkoutDB());
        assertNotNull(persistenceManager.getWorkoutSessionDB());
        
        // And we should still be initialized
        assertTrue(persistenceManager.isInitialized());
    }
    
    @Test
    public void testShutdown() throws DataAccessException {
        // Initialize first
        persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, false);
        assertTrue(persistenceManager.isInitialized());
        
        // Then shutdown
        persistenceManager.shutdown();
        
        // Should be reset to not initialized
        assertFalse(persistenceManager.isInitialized());
    }
    
    @Test
    public void testStaticFactoryMethods() {
        // First test with useReal=false, should work without initialization
        IExerciseDB exerciseDB = PersistenceManager.getExerciseDB(false);
        IWorkoutDB workoutDB = PersistenceManager.getWorkoutDB(false);
        IWorkoutSessionDB sessionDB = PersistenceManager.getWorkoutSessionDB(false);
        
        assertNotNull(exerciseDB);
        assertNotNull(workoutDB);
        assertNotNull(sessionDB);
        
        // Now initialize and test with useReal=true
        try {
            persistenceManager.initialize(TEST_SCRIPT_PATH, TEST_CONFIG_PATH, true, false);
            
            exerciseDB = PersistenceManager.getExerciseDB(true);
            workoutDB = PersistenceManager.getWorkoutDB(true);
            sessionDB = PersistenceManager.getWorkoutSessionDB(true);
            
            assertNotNull(exerciseDB);
            assertNotNull(workoutDB);
            assertNotNull(sessionDB);
        } catch (DataAccessException e) {
            // Just catch and continue, the point is to test the interface not actual DB access
        }
    }
}