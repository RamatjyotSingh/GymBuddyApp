package comp3350.gymbuddy.tests.integration;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

import static org.junit.Assert.*;
import timber.log.Timber;

/**
 * Integration tests for WorkoutSessionHSQLDB
 * Each test is completely independent and follows AAA pattern:
 * - Arrange: Set up the test data
 * - Act: Perform the action being tested
 * - Assert: Verify the expected outcome
 */
@RunWith(AndroidJUnit4.class)
public class WorkoutSessionDBIntegrationTest extends DBIntegrationTestHelper {
    private static final String TAG = "WorkoutSessionDBTest";
    
    // Database access objects
    private IWorkoutSessionDB sessionDB;
    private IWorkoutDB workoutDB;
    private IExerciseDB exerciseDB;
    
    // Used for generating unique IDs
    private static final AtomicInteger nextId = new AtomicInteger(1000);
    
    /**
     * Set up before each test
     */
    @Before
    @Override
    public void setUp() {
        super.setUp();
        
        // Initialize database access objects
        sessionDB = new WorkoutSessionHSQLDB();
        workoutDB = new WorkoutHSQLDB();
        exerciseDB = new ExerciseHSQLDB();
        
        Timber.tag(TAG).d("Test setup complete");
    }
    
    /**
     * Test inserting and retrieving a workout session
     */
    @Test
    public void testInsertAndGetWorkoutSession() {
        Timber.tag(TAG).d("Starting testInsertAndGetWorkoutSession");
        
        try {
            // ARRANGE
            int sessionId = getNextId();
            WorkoutProfile profile = createTestProfile();
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 3600000; // 1 hour later
            WorkoutSession session = createWorkoutSession(sessionId, profile, startTime, endTime);
            
            // ACT
            boolean inserted = sessionDB.insertSession(session);
            WorkoutSession retrieved = sessionDB.getWorkoutSessionByid(sessionId);
            
            // ASSERT
            assertTrue("Session should be inserted successfully", inserted);
            assertNotNull("Retrieved session should not be null", retrieved);
            assertEquals("Session ID should match", sessionId, retrieved.getId());
            assertEquals("Start time should match", startTime, retrieved.getStartTime());
            assertEquals("End time should match", endTime, retrieved.getEndTime());
            assertEquals("Profile ID should match", profile.getID(), retrieved.getWorkoutProfile().getID());
            
            assertNotNull("Session should have workout items", retrieved.getWorkoutItems());
            assertEquals("Session should have one workout item", 1, retrieved.getWorkoutItems().size());
            
            Timber.tag(TAG).d("testInsertAndGetWorkoutSession successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Test updating a session's end time
     */
    @Test
    public void testUpdateSessionEndTime() {
        Timber.tag(TAG).d("Starting testUpdateSessionEndTime");
        
        try {
            // ARRANGE
            int sessionId = getNextId();
            WorkoutProfile profile = createTestProfile();
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 3600000;
            
            // Create and insert the session
            WorkoutSession session = createAndInsertWorkoutSession(sessionId, profile, startTime, endTime);
            long newEndTime = System.currentTimeMillis() + 7200000; // 2 hours later
            
            // ACT
            boolean updated = sessionDB.updateSessionEndTime(sessionId, newEndTime);
            WorkoutSession retrievedSession = sessionDB.getWorkoutSessionByid(sessionId);
            
            // ASSERT
            assertTrue("Session end time should be updated", updated);
            assertNotNull("Retrieved session should not be null", retrievedSession);
            assertEquals("End time should be updated", newEndTime, retrievedSession.getEndTime());
            
            Timber.tag(TAG).d("testUpdateSessionEndTime successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Test adding an exercise to a session
     */
    @Test
    public void testAddExerciseToSession() {
        Timber.tag(TAG).d("Starting testAddExerciseToSession");
        
        try {
            // ARRANGE
            int sessionId = getNextId();
            WorkoutProfile profile = createTestProfile();
            WorkoutSession session = createAndInsertWorkoutSession(sessionId, profile);
            
            // Verify initial state
            List<WorkoutItem> initialItems = sessionDB.getExercisesForSession(sessionId);
            assertEquals("Session should start with one exercise", 1, initialItems.size());
            
            // Get second exercise for adding
            Exercise exercise2 = exerciseDB.getExerciseByID(2);
            assertNotNull("Exercise with ID 2 should exist", exercise2);
            WorkoutItem newItem = new WorkoutItem(exercise2, 1, 60.0); // Timed exercise
            
            // ACT
            boolean added = sessionDB.addExerciseToSession(sessionId, newItem);
            List<WorkoutItem> updatedItems = sessionDB.getExercisesForSession(sessionId);
            
            // ASSERT
            assertTrue("Exercise should be added to session", added);
            assertEquals("Session should now have 2 exercises", 2, updatedItems.size());
            
            // Check one of the items is the new exercise
            boolean found = false;
            for (WorkoutItem item : updatedItems) {
                if (item.getExercise().getID() == exercise2.getID()) {
                    found = true;
                    assertEquals("Time should match", 60.0, item.getTime(), 0.01);
                    break;
                }
            }
            assertTrue("New exercise should be found in session items", found);
            
            Timber.tag(TAG).d("testAddExerciseToSession successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Test removing an exercise from a session
     */
    @Test
    public void testRemoveExerciseFromSession() {
        Timber.tag(TAG).d("Starting testRemoveExerciseFromSession");
        
        try {
            // ARRANGE
            int sessionId = getNextId();
            WorkoutProfile profile = createTestProfile();
            WorkoutSession session = createAndInsertWorkoutSession(sessionId, profile);
            
            // Add a second exercise
            Exercise exercise2 = exerciseDB.getExerciseByID(2);
            assertNotNull("Exercise with ID 2 should exist", exercise2);
            WorkoutItem newItem = new WorkoutItem(exercise2, 1, 60.0);
            boolean added = sessionDB.addExerciseToSession(sessionId, newItem);
            assertTrue("Second exercise should be added", added);
            
            // Verify we have 2 exercises
            List<WorkoutItem> beforeItems = sessionDB.getExercisesForSession(sessionId);
            assertEquals("Session should have 2 exercises before removal", 2, beforeItems.size());
            
            // Get first exercise for removal
            Exercise exercise1 = exerciseDB.getExerciseByID(1);
            assertNotNull("Exercise with ID 1 should exist", exercise1);
            
            // ACT
            boolean removed = sessionDB.removeExerciseFromSession(sessionId, exercise1.getID());
            List<WorkoutItem> afterItems = sessionDB.getExercisesForSession(sessionId);
            
            // ASSERT
            assertTrue("Exercise should be removed from session", removed);
            assertEquals("Session should have one less exercise", 1, afterItems.size());
            
            // Ensure the removed exercise is no longer present
            boolean found = false;
            for (WorkoutItem item : afterItems) {
                if (item.getExercise().getID() == exercise1.getID()) {
                    found = true;
                    break;
                }
            }
            assertFalse("Removed exercise should not be found in session items", found);
            
            Timber.tag(TAG).d("testRemoveExerciseFromSession successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Test deleting a session
     */
    @Test
    public void testDeleteSession() {
        Timber.tag(TAG).d("Starting testDeleteSession");
        
        try {
            // ARRANGE
            int sessionId = getNextId();
            WorkoutProfile profile = createTestProfile();
            WorkoutSession session = createAndInsertWorkoutSession(sessionId, profile);
            
            // Verify session exists
            WorkoutSession beforeDelete = sessionDB.getWorkoutSessionByid(sessionId);
            assertNotNull("Session should exist before deletion", beforeDelete);
            
            // ACT
            boolean deleted = sessionDB.deleteSession(sessionId);
            WorkoutSession afterDelete = sessionDB.getWorkoutSessionByid(sessionId);
            List<WorkoutItem> items = sessionDB.getExercisesForSession(sessionId);
            
            // ASSERT
            assertTrue("Session should be deleted successfully", deleted);
            assertNull("Session should not exist after deletion", afterDelete);
            assertTrue("Session items should be empty after deletion", items.isEmpty());
            
            Timber.tag(TAG).d("testDeleteSession successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Test retrieving all sessions
     */
    @Test
    public void testGetAllSessions() {
        Timber.tag(TAG).d("Starting testGetAllSessions");
        
        try {
            // ARRANGE
            // Create several sessions
            WorkoutProfile profile = createTestProfile();
            int sessionId1 = getNextId();
            int sessionId2 = getNextId();
            
            createAndInsertWorkoutSession(sessionId1, profile);
            createAndInsertWorkoutSession(sessionId2, profile);
            
            // ACT
            List<WorkoutSession> allSessions = sessionDB.getAll();
            
            // ASSERT
            assertNotNull("Session list should not be null", allSessions);
            assertTrue("Should have at least 2 sessions", allSessions.size() >= 2);
            
            // Verify our sessions are in the list
            boolean foundSession1 = false;
            boolean foundSession2 = false;
            
            for (WorkoutSession session : allSessions) {
                if (session.getId() == sessionId1) foundSession1 = true;
                if (session.getId() == sessionId2) foundSession2 = true;
            }
            
            assertTrue("Should find session 1 in results", foundSession1);
            assertTrue("Should find session 2 in results", foundSession2);
            
            Timber.tag(TAG).d("testGetAllSessions successful");
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Test failed");
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /* -------------------- Helper Methods -------------------- */
    
    /**
     * Get a unique ID for test sessions
     */
    private int getNextId() {
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("CALL NEXT VALUE FOR workout_session_id_sequence")) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Timber.tag(TAG).w(e, "Failed to get ID from sequence");
        }
        
        // Fallback to atomic counter
        return nextId.getAndIncrement();
    }
    
    /**
     * Helper to create a test workout profile with a unique name
     */
    private WorkoutProfile createTestProfile() throws DBException {
        List<WorkoutItem> items = new ArrayList<>();
        String uniqueName = "TestProfile_" + System.currentTimeMillis();
        WorkoutProfile profile = new WorkoutProfile(uniqueName, "test_icon.png", items);
        
        boolean saved = workoutDB.saveWorkout(profile);
        assertTrue("Profile should be saved successfully", saved);
        
        List<WorkoutProfile> profiles = workoutDB.getAll();
        for (WorkoutProfile p : profiles) {
            if (p.getName().equals(uniqueName)) {
                return p;
            }
        }
        
        fail("Failed to retrieve created test profile");
        return null; // Never reached
    }
    
    /**
     * Helper to create a workout session object (without inserting it)
     */
    private WorkoutSession createWorkoutSession(int sessionId, WorkoutProfile profile,
                                                long startTime, long endTime) throws DBException {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        Exercise exercise = exerciseDB.getExerciseByID(1);
        assertNotNull("Exercise with ID 1 should exist", exercise);
        
        workoutItems.add(new WorkoutItem(exercise, 3, 12, 50.0));
        
        return new WorkoutSession(sessionId, startTime, endTime, workoutItems, profile);
    }
    
    /**
     * Helper method to create a workout session with default times
     */
    private WorkoutSession createWorkoutSession(int sessionId, WorkoutProfile profile) throws DBException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 3600000; // 1 hour later
        return createWorkoutSession(sessionId, profile, startTime, endTime);
    }
    
    /**
     * Helper to create and insert a workout session
     */
    private WorkoutSession createAndInsertWorkoutSession(int sessionId, WorkoutProfile profile,
                                                        long startTime, long endTime) throws DBException {
        WorkoutSession session = createWorkoutSession(sessionId, profile, startTime, endTime);
        boolean inserted = sessionDB.insertSession(session);
        assertTrue("Session should be inserted successfully", inserted);
        return session;
    }
    
    /**
     * Helper to create and insert a workout session with default times
     */
    private WorkoutSession createAndInsertWorkoutSession(int sessionId, WorkoutProfile profile) throws DBException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 3600000; // 1 hour later
        return createAndInsertWorkoutSession(sessionId, profile, startTime, endTime);
    }
    
    @After
    @Override
    public void tearDown() {
        super.tearDown();
    }
}