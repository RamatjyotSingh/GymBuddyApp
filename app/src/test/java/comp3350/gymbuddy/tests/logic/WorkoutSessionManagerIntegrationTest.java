package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void testSearch(){
        final List<WorkoutSession> result;
        final List<WorkoutSession> expected = new ArrayList<>();
        final String searchString;

        expected.add(workoutSessionStub.getWorkoutSessionByid(0));
        searchString = expected.get(0).getDate();

        result = workoutSessionManager.search(searchString);
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0), result.get(0));
    }

    @Test
    public void testInvalidSearch(){
        final List<WorkoutSession> result;
        final String searchString = "12345";

        result = workoutSessionManager.search(searchString);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullSearch(){
        final List<WorkoutSession> result;

        result = workoutSessionManager.search(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
