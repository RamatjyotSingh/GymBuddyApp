package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutManagerTest {
    private IWorkoutDB workoutStub;
    private WorkoutManager workoutManager;

    @Before
    public void setup(){
        workoutStub = PersistenceManager.getWorkoutDB(false);
        workoutManager = new WorkoutManager(false);
    }

    @Test
    public void testGetSavedWorkouts(){
        List<WorkoutProfile> result;
        List<WorkoutProfile> expected;

        expected = workoutStub.getAll();
        result = workoutManager.getSavedWorkouts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expected, result);
    }

    @Test
    public void testSaveWorkout(){
        List<WorkoutProfile> workoutProfiles;
        int beforeSize = workoutStub.getAll().size();

        WorkoutProfile newProfile = new WorkoutProfile("Test Profile", "path", new ArrayList<>());

        workoutManager.saveWorkout(newProfile);

        workoutProfiles = workoutStub.getAll();

        assertTrue(workoutStub.getAll().size() > beforeSize);

        WorkoutProfile addedProfile = workoutProfiles.get(workoutProfiles.size() - 1);
        assertEquals(addedProfile.getName(), newProfile.getName());
        assertEquals(addedProfile.getIconPath(), newProfile.getIconPath());
        assertEquals(addedProfile.getWorkoutItems(), newProfile.getWorkoutItems());
    }

    @Test
    public void testInvalidSaveWorkout(){
        int beforeSize = workoutStub.getAll().size();

        workoutManager.saveWorkout(null);

        assertEquals(workoutStub.getAll().size(), beforeSize);
    }

    @Test
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
