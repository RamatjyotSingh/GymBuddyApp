package comp3350.gymbuddy.tests.persistence;

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
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WorkoutSessionDAOTest {
    private static HSQLDBTestHelper dbHelper;
    private IWorkoutSessionDB sessionDB;
    private IWorkoutDB workoutDB;
    private IExerciseDB exerciseDB;
    
    @Before
    public void setUp() throws Exception {
        dbHelper = new HSQLDBTestHelper();
        dbHelper.setUp();
        sessionDB = dbHelper.getFactory().createWorkoutSessionDB();
        workoutDB = dbHelper.getFactory().createWorkoutDB();
        exerciseDB = dbHelper.getFactory().createExerciseDB();
    }
    
    @After
    public void tearDown() throws Exception {
        dbHelper.tearDown();
    }
    
    @Test
    public void testGetAll() throws DBException {
        // Get all workout sessions
        List<WorkoutSession> sessions = sessionDB.getAll();
        
        // Verify results
        assertNotNull("Session list should not be null", sessions);
        assertFalse("Session list should not be empty", sessions.isEmpty());
        
        // Based on the Project.script file, we expect at least 1 session
        assertTrue("Should have at least 1 session", sessions.size() >= 1);
    }
    
    @Test
    public void testInsertAndGetSession() throws DBException {
        // Get a workout profile to use
        List<WorkoutProfile> profiles = workoutDB.getAll();
        assertTrue("Need at least one profile", profiles.size() > 0);
        WorkoutProfile profile = profiles.get(0);
        
        // Create session items
        List<WorkoutItem> items = new ArrayList<>();
        Exercise exercise = exerciseDB.getExerciseByID(1); // Push-up
        items.add(new WorkoutItem(exercise, 1, 10, 0));
        
        // Create a new session
        long startTime = System.currentTimeMillis() - 3600000; // 1 hour ago
        long endTime = System.currentTimeMillis();
        int sessionId = 100; // Unique ID for this test
        
        WorkoutSession session = new WorkoutSession(sessionId, startTime, endTime, items, profile);
        
        // Insert the session
        boolean inserted = sessionDB.insertSession(session);
        assertTrue("Session should be inserted successfully", inserted);
        
        // Get the session by ID
        WorkoutSession retrieved = sessionDB.getWorkoutSessionByid(sessionId);
        assertNotNull("Retrieved session should not be null", retrieved);
        assertEquals("Retrieved session should have correct ID", sessionId, retrieved.getId());
        assertEquals("Retrieved session should have correct start time", startTime, retrieved.getStartTime());
        assertEquals("Retrieved session should have correct end time", endTime, retrieved.getEndTime());
        
        // Check session items
        List<WorkoutItem> retrievedItems = retrieved.getSessionItems();
        assertNotNull("Retrieved items should not be null", retrievedItems);
        assertEquals("Retrieved session should have 1 item", 1, retrievedItems.size());
        assertEquals("Item should have exercise ID 1", 1, retrievedItems.get(0).getExercise().getID());
    }
    
    @Test
    public void testUpdateSessionEndTime() throws DBException {
        // Insert a session first
        testInsertAndGetSession();
        
        // Get the session
        WorkoutSession session = sessionDB.getWorkoutSessionByid(100);
        assertNotNull("Session should exist", session);
        
        // Update end time
        long newEndTime = System.currentTimeMillis();
        boolean updated = sessionDB.updateSessionEndTime(100, newEndTime);
        assertTrue("End time should be updated successfully", updated);
        
        // Get the session again to verify update
        WorkoutSession updated_session = sessionDB.getWorkoutSessionByid(100);
        assertEquals("End time should be updated", newEndTime, updated_session.getEndTime());
    }
    
    @Test
    public void testDeleteSession() throws DBException {
        // Insert a session first
        testInsertAndGetSession();
        
        // Delete the session
        boolean deleted = sessionDB.deleteSession(100);
        assertTrue("Session should be deleted successfully", deleted);
        
        // Try to get it
        WorkoutSession deleted_session = sessionDB.getWorkoutSessionByid(100);
        assertEquals("Deleted session should not be retrievable", null, deleted_session);
    }
    
    @Test
    public void testSearch() throws DBException {
        // Insert a session with a known profile
        WorkoutProfile profile = new WorkoutProfile(200, "SearchTestProfile", null, new ArrayList<>(), false);
        workoutDB.saveWorkout(profile);
        
        profile = workoutDB.getWorkoutProfileByIdIncludingDeleted(200);
        assertNotNull("Profile should be created", profile);
        
        // Create and insert session
        WorkoutSession session = new WorkoutSession(200, System.currentTimeMillis(), System.currentTimeMillis(), 
                                                  new ArrayList<>(), profile);
        sessionDB.insertSession(session);
        
        // Search for the profile name
        List<WorkoutSession> results = sessionDB.search("SearchTest");
        assertFalse("Search results should not be empty", results.isEmpty());
        
        boolean found = false;
        for (WorkoutSession s : results) {
            if (s.getId() == 200) {
                found = true;
                break;
            }
        }
        
        assertTrue("Should find session when searching for profile name", found);
    }
}