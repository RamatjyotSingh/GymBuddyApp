package comp3350.gymbuddy.tests.integration;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WorkoutDBIntegrationTest extends DBIntegrationTestHelper {
    
    @Test
    public void testSaveAndGetWorkoutProfile() {
        try {
            IWorkoutDB workoutDB = new WorkoutHSQLDB();
            IExerciseDB exerciseDB = new ExerciseHSQLDB();
            
            // Get an exercise to add to the profile
            Exercise exercise = exerciseDB.getExerciseByID(1); // Assuming ID 1 exists
            assertNotNull("Exercise with ID 1 should exist", exercise);
            
            // Create workout items
            List<WorkoutItem> items = new ArrayList<>();
            items.add(new WorkoutItem(exercise, 3, 12, 50.0));
            
            // Get initial count
            int initialCount = workoutDB.getAll().size();
            
            // Create a new workout profile
            String uniqueName = "Integration Test Profile " + System.currentTimeMillis();
            WorkoutProfile profile = new WorkoutProfile(uniqueName, "profile_icon.png", items);
            
            // Save the profile
            boolean saved = workoutDB.saveWorkout(profile);
            assertTrue("Profile should be saved successfully", saved);
            
            // Get updated profiles list
            List<WorkoutProfile> updatedProfiles = workoutDB.getAll();
            assertEquals("Profile count should increase by 1", initialCount + 1, updatedProfiles.size());
            
            // Find our new profile
            WorkoutProfile savedProfile = null;
            for (WorkoutProfile p : updatedProfiles) {
                if (p.getName().equals(uniqueName)) {
                    savedProfile = p;
                    break;
                }
            }
            
            assertNotNull("Saved profile should be found", savedProfile);
            
            // Get profile by ID
            WorkoutProfile retrieved = workoutDB.getWorkoutProfileById(savedProfile.getID());
            assertNotNull("Retrieved profile should not be null", retrieved);
            assertEquals("Profile name should match", uniqueName, retrieved.getName());
            
            // Check workout items
            assertNotNull("Profile workout items should not be null", retrieved.getWorkoutItems());
            assertFalse("Profile should have workout items", retrieved.getWorkoutItems().isEmpty());
            assertEquals("Profile should have one workout item", 1, retrieved.getWorkoutItems().size());
            
            WorkoutItem retrievedItem = retrieved.getWorkoutItems().get(0);
            assertEquals("Item exercise ID should match", exercise.getID(), retrievedItem.getExercise().getID());
            assertEquals("Item reps should match", 12, retrievedItem.getReps());
            assertEquals("Item weight should match", 50.0, retrievedItem.getWeight(), 0.01);
            
        } catch (DBException e) {
            fail("Database exception: " + e.getMessage());
        }
    }
}