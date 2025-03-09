package comp3350.gymbuddy.tests.integration;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseDBIntegrationTest extends DBIntegrationTestHelper {
    
    @Test
    public void testGetAllExercises() {
        IExerciseDB exerciseDB = new ExerciseHSQLDB();
        
        try {
            List<Exercise> exercises = exerciseDB.getAll();
            assertNotNull("Exercise list should not be null", exercises);
            assertFalse("Exercise list should not be empty (initial data)", exercises.isEmpty());
            
            // Verify expected initial exercise exists - assuming "Push-Up" is in insert_data.sql
            boolean foundPushUp = false;
            for (Exercise exercise : exercises) {
                if ("Push-Up".equals(exercise.getName())) {
                    foundPushUp = true;
                    break;
                }
            }
            assertTrue("Should find Push-Up exercise in database", foundPushUp);
            
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetExerciseByID() {
        IExerciseDB exerciseDB = new ExerciseHSQLDB();
        
        try {
            // Assuming exercise with ID 1 exists from initial data
            Exercise exercise = exerciseDB.getExerciseByID(1);
            assertNotNull("Exercise with ID 1 should exist", exercise);
            assertEquals("Exercise ID should match", 1, exercise.getID());
            
            // Test non-existent ID
            Exercise nonExistent = exerciseDB.getExerciseByID(999);
            assertNull("Exercise with ID 999 should not exist", nonExistent);
            
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
}