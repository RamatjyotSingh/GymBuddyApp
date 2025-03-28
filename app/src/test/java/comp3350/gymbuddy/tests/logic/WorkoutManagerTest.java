package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.exception.WorkoutAccessException;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutManagerTest {
    private IWorkoutDB workoutStub;
    private WorkoutManager workoutManager;

    @Before
    public void setup() {
        // Get a stub database implementation
        workoutStub = PersistenceManager.getWorkoutDB(false);
        
        // Create workout manager with the stub database
        workoutManager = new WorkoutManager(workoutStub);
    }

    @Test
    public void testGetSavedWorkouts() {
        // Get all workouts from the stub
        List<WorkoutProfile> expectedProfiles = workoutStub.getAll();
        
        // Filter out deleted profiles to match what getSavedWorkouts would return
        List<WorkoutProfile> expectedSavedProfiles = new ArrayList<>();
        for (WorkoutProfile profile : expectedProfiles) {
            if (!profile.isDeleted()) {
                expectedSavedProfiles.add(profile);
            }
        }
        
        // Get saved workouts through the manager
        List<WorkoutProfile> result = workoutManager.getSavedWorkouts();

        assertEquals(result, expectedProfiles);
    }

    @Test
    public void testSaveWorkout() {
        // Get count before adding new profile
        int beforeSize = workoutManager.getSavedWorkouts().size();
        
        // Create a new workout profile to save
        WorkoutProfile newProfile = new WorkoutProfile("Test Profile", "icon_path.png", new ArrayList<>());
        
        // Save the profile
        workoutManager.saveWorkout(newProfile);
        

        
        // Get updated list of workouts
        List<WorkoutProfile> updatedProfiles = workoutManager.getSavedWorkouts();
        
        // Verify a new workout was added
        assertEquals(beforeSize + 1, updatedProfiles.size());
        
        // Find the added profile
        WorkoutProfile addedProfile = null;
        for (WorkoutProfile profile : updatedProfiles) {
            if (profile.getName().equals(newProfile.getName())) {
                addedProfile = profile;
                break;
            }
        }
        
        // Verify the added profile has the correct data
        assertNotNull("Added profile not found", addedProfile);
        assertEquals(newProfile.getName(), addedProfile.getName());
        assertEquals(newProfile.getIconPath(), addedProfile.getIconPath());
        assertEquals(newProfile.getWorkoutItems().size(), addedProfile.getWorkoutItems().size());
    }

    @Test
    public void testGetWorkoutProfileByID() {
        // Get a profile from the stub
        WorkoutProfile expected = workoutStub.getAll().get(0);
        int profileId = expected.getID();
        
        // Get the profile through the manager
        WorkoutProfile result = workoutManager.getWorkoutProfileByID(profileId);
        
        // Verify result
        assertNotNull(result);
        assertEquals(expected.getID(), result.getID());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getIconPath(), result.getIconPath());
    }

    @Test(expected = WorkoutAccessException.class)
    public void testGetInvalidWorkoutProfileByID() {
        // This should throw an exception since the WorkoutManager throws an exception
        // when a workout is not found
        workoutManager.getWorkoutProfileByID(-999);
    }


    @Test(expected = WorkoutAccessException.class)
    public void testGetWorkoutProfileByID_NotFound() {
        WorkoutProfile result = workoutManager.getWorkoutProfileByID(999);
        assertNull(result);
    }

    @Test
    public void testDeleteWorkout() {
        int initialSize = workoutStub.getAll().size();
        workoutManager.deleteWorkout(1);

        assertEquals(initialSize - 1, workoutStub.getAll().size());
        assertNull(workoutStub.getWorkoutProfileById(1));
    }

    @Test
    public void testDeleteWorkout_NonExistent() {
        int initialSize = workoutStub.getAll().size();
        workoutManager.deleteWorkout(999); //Should not throw exception
        assertEquals(initialSize, workoutStub.getAll().size());
    }

}
