package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutManagerIntegrationTest {
    private IWorkoutDB workoutStub;
    private WorkoutManager workoutManager;

    @Before
    public void setup(){
        workoutStub = PersistenceManager.getWorkoutDB(true);
        workoutManager = new WorkoutManager(true);
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

        WorkoutProfile newProfile = new WorkoutProfile("Test Profile", "path", null);

        workoutManager.saveWorkout(newProfile);

        workoutProfiles = workoutStub.getAll();

        assertTrue(workoutStub.getAll().size() > beforeSize);
        assertEquals(workoutProfiles.get(workoutProfiles.size()-1), newProfile);
    }

    @Test
    public void testInvalidSaveWorkout(){
        int beforeSize = workoutStub.getAll().size();

        workoutManager.saveWorkout(null);

        assertEquals(workoutStub.getAll().size(), beforeSize);
    }
}
