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
    public void testGetAll(){
        List<WorkoutProfile> result;
        List<WorkoutProfile> expected;

        expected = workoutStub.getAll();
        result = workoutManager.getAll();

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
}
