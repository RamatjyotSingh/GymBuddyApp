package comp3350.gymbuddy.tests.integration;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WorkoutSessionDBIntegrationTest extends DBIntegrationTestHelper {
    private IWorkoutSessionDB sessionDB;
    private IWorkoutDB workoutDB;
    private IExerciseDB exerciseDB;
    
    @Before
    public void setUp() {
        super.setUp();
        sessionDB = new WorkoutSessionHSQLDB();
        workoutDB = new WorkoutHSQLDB();
        exerciseDB = new ExerciseHSQLDB();
    }
    
    @Test
    public void testInsertAndGetWorkoutSession() {
        try {
            // First create a workout profile to use
            WorkoutProfile profile = createTestProfile();
            assertNotNull("Test profile should be created", profile);
            
            // Now create a workout session
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 3600000; // 1 hour later
            
            List<WorkoutItem> workoutItems = new ArrayList<>();
            Exercise exercise = exerciseDB.getExerciseByID(1); // Assuming ID 1 exists
            
            if (exercise != null) {
                workoutItems.add(new WorkoutItem(exercise, 3, 12, 50.0));
                
                // Create session with ID 100 for testing
                WorkoutSession session = new WorkoutSession(100, startTime, endTime, workoutItems, profile);
                
                // Save session
                boolean inserted = sessionDB.insertSession(session);
                assertTrue("Session should be inserted successfully", inserted);
                
                // Retrieve session
                WorkoutSession retrieved = sessionDB.getWorkoutSessionByid(100);
                assertNotNull("Retrieved session should not be null", retrieved);
                assertEquals("Session ID should match", 100, retrieved.getId());
                assertEquals("Start time should match", startTime, retrieved.getStartTime());
                assertEquals("End time should match", endTime, retrieved.getEndTime());
                assertEquals("Profile should match", profile.getId(), retrieved.getWorkoutProfile().getId());
                
                // Check workout items
                assertNotNull("Session should have workout items", retrieved.getWorkoutItems());
                assertFalse("Workout items should not be empty", retrieved.getWorkoutItems().isEmpty());
                assertEquals("Session should have one workout item", 1, retrieved.getWorkoutItems().size());
            } else {
                fail("Could not get exercise with ID 1 - check your test database initialization");
            }
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpdateSessionEndTime() {
        try {
            // First create a session
            testInsertAndGetWorkoutSession();
            
            // Now update its end time
            long newEndTime = System.currentTimeMillis() + 7200000; // 2 hours later
            boolean updated = sessionDB.updateSessionEndTime(100, newEndTime);
            assertTrue("Session end time should be updated", updated);
            
            // Verify the update
            WorkoutSession updated_session = sessionDB.getWorkoutSessionByid(100);
            assertNotNull("Retrieved session should not be null", updated_session);
            assertEquals("End time should be updated", newEndTime, updated_session.getEndTime());
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddExerciseToSession() {
        try {
            // First create a session
            testInsertAndGetWorkoutSession();
            
            // Now add another exercise
            Exercise exercise2 = exerciseDB.getExerciseByID(2); // Assuming ID 2 exists
            
            if (exercise2 != null) {
                WorkoutItem newItem = new WorkoutItem(exercise2, 1, 60.0); // Timed exercise
                boolean added = sessionDB.addExerciseToSession(100, newItem);
                assertTrue("Exercise should be added to session", added);
                
                // Verify the addition
                List<WorkoutItem> items = sessionDB.getExercisesForSession(100);
                assertEquals("Session should now have 2 exercises", 2, items.size());
                
                // Check one of the items is the new exercise
                boolean found = false;
                for (WorkoutItem item : items) {
                    if (item.getExercise().getID() == exercise2.getID()) {
                        found = true;
                        assertEquals("Time should match", 60.0, item.getTime(), 0.01);
                        break;
                    }
                }
                assertTrue("New exercise should be found in session items", found);
            } else {
                fail("Could not get exercise with ID 2 - check your test database initialization");
            }
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testRemoveExerciseFromSession() {
        try {
            // Add two exercises to a session
            testAddExerciseToSession();
            
            // Count items before removal
            List<WorkoutItem> beforeItems = sessionDB.getExercisesForSession(100);
            int beforeCount = beforeItems.size();
            
            // Remove one exercise
            Exercise exercise = exerciseDB.getExerciseByID(1);
            boolean removed = sessionDB.removeExerciseFromSession(100, exercise.getID());
            assertTrue("Exercise should be removed from session", removed);
            
            // Verify the removal
            List<WorkoutItem> afterItems = sessionDB.getExercisesForSession(100);
            assertEquals("Session should have one less exercise", beforeCount - 1, afterItems.size());
            
            // Ensure the removed exercise is no longer present
            boolean found = false;
            for (WorkoutItem item : afterItems) {
                if (item.getExercise().getID() == exercise.getID()) {
                    found = true;
                    break;
                }
            }
            assertFalse("Removed exercise should not be found in session items", found);
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteSession() {
        try {
            // First create a session
            testInsertAndGetWorkoutSession();
            
            // Verify it exists
            WorkoutSession session = sessionDB.getWorkoutSessionByid(100);
            assertNotNull("Session should exist before deletion", session);
            
            // Delete it
            boolean deleted = sessionDB.deleteSession(100);
            assertTrue("Session should be deleted successfully", deleted);
            
            // Verify it's gone
            WorkoutSession afterDelete = sessionDB.getWorkoutSessionByid(100);
            assertNull("Session should not exist after deletion", afterDelete);
            
            // Verify associated items are also deleted
            List<WorkoutItem> items = sessionDB.getExercisesForSession(100);
            assertTrue("Session items should be empty after deletion", items.isEmpty());
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to create a test profile for workout sessions
     */
    private WorkoutProfile createTestProfile() throws DBException {
        List<WorkoutItem> items = new ArrayList<>();
        String name = "Test Integration Profile " + System.currentTimeMillis();
        WorkoutProfile profile = new WorkoutProfile(name, "test_icon.png", items);
        
        workoutDB.saveWorkout(profile);
        
        List<WorkoutProfile> profiles = workoutDB.getAll();
        for (WorkoutProfile p : profiles) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        
        return null;
    }
}