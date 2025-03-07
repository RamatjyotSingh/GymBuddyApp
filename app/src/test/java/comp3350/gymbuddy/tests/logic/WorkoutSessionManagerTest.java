package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

public class WorkoutSessionManagerTest {
    private IWorkoutSessionDB workoutSessionStub;
    private WorkoutSessionManager workoutSessionManager;

    @Before
    public void setup(){
        workoutSessionStub = PersistenceManager.getWorkoutSessionDB(false);
        workoutSessionManager = new WorkoutSessionManager(false);
    }

    @Test
    public void testGetAll(){
        final List<WorkoutSession> result;
        final List<WorkoutSession> expected;

        expected = workoutSessionStub.getAll();
        result = workoutSessionManager.getAll();

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWorkoutSessionByID(){
        final WorkoutSession result;
        final WorkoutSession expected;

        expected = workoutSessionStub.getWorkoutSessionByid(0);
        result = workoutSessionManager.getWorkoutSessionByID(0);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testInvalidGetWorkoutSessionByID(){
        final WorkoutSession result;

        result = workoutSessionManager.getWorkoutSessionByID(-1);

        assertNull(result);
    }
}
