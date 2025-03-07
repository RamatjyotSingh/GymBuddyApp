package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

public class WorkoutSessionManagerIntegrationTest {
    private IWorkoutSessionDB workoutSessionStub;
    private WorkoutSessionManager workoutSessionManager;

    @Before
    public void setup(){
        workoutSessionStub = PersistenceManager.getWorkoutSessionDB(true);
        workoutSessionManager = new WorkoutSessionManager(true);
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
