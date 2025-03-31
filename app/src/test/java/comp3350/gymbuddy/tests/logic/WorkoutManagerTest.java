package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.logic.exception.WorkoutAccessException;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutManagerTest {
    @Mock private IWorkoutDB mockWorkoutDB;
    private WorkoutManager workoutManager;

    private WorkoutProfile testProfile1;
    private WorkoutProfile testProfile2;
    private List<WorkoutProfile> testProfiles;

    @Before
    public void setup() {
        workoutManager = new WorkoutManager(mockWorkoutDB);

        // Create test data
        testProfile1 = new WorkoutProfile("Morning Routine", "path1", new ArrayList<>());
        testProfile2 = new WorkoutProfile("Evening Routine", "path2", new ArrayList<>());
        testProfiles = new ArrayList<>();
        testProfiles.add(testProfile1);
        testProfiles.add(testProfile2);
    }

    @Test
    public void testConstructor() {
        assertNotNull(workoutManager);
        verify(mockWorkoutDB, never()).getAll(); //Verify no DB interaction on construction
    }

    @Test
    public void testGetSavedWorkouts_Success() throws DBException {
        when(mockWorkoutDB.getAll()).thenReturn(testProfiles);

        List<WorkoutProfile> result = workoutManager.getSavedWorkouts();

        assertEquals(2, result.size());
        assertTrue(result.contains(testProfile1));
        assertTrue(result.contains(testProfile2));
        verify(mockWorkoutDB).getAll();
    }

    @Test(expected = WorkoutAccessException.class)
    public void testGetSavedWorkouts_DBException() throws DBException {
        when(mockWorkoutDB.getAll()).thenThrow(new DBException("Database error"));

        workoutManager.getSavedWorkouts();
    }

    @Test
    public void testSaveWorkout_Success() throws DBException {
        workoutManager.saveWorkout(testProfile1);
        verify(mockWorkoutDB).saveWorkout(testProfile1);
    }

    @Test(expected = WorkoutAccessException.class)
    public void testSaveWorkout_DBException() throws DBException {
        doThrow(new DBException("Save failed")).when(mockWorkoutDB).saveWorkout(any());

        workoutManager.saveWorkout(testProfile1);
    }

    @Test
    public void testGetWorkoutProfileByID_Success() throws DBException {
        when(mockWorkoutDB.getWorkoutProfileById(1)).thenReturn(testProfile1);

        WorkoutProfile result = workoutManager.getWorkoutProfileByID(1);

        assertEquals(testProfile1, result);
        verify(mockWorkoutDB).getWorkoutProfileById(1);
    }

    @Test(expected = WorkoutAccessException.class)
    public void testGetWorkoutProfileByID_NotFound() throws DBException {
        when(mockWorkoutDB.getWorkoutProfileById(1)).thenReturn(null);

        workoutManager.getWorkoutProfileByID(1);
    }

    @Test(expected = WorkoutAccessException.class)
    public void testGetWorkoutProfileByID_DBException() throws DBException {
        when(mockWorkoutDB.getWorkoutProfileById(1)).thenThrow(new DBException("DB error"));

        workoutManager.getWorkoutProfileByID(1);
    }

    @Test
    public void testDeleteWorkout_Success() throws DBException {
        workoutManager.deleteWorkout(1);
        verify(mockWorkoutDB).deleteWorkout(1);
    }

    @Test(expected = WorkoutAccessException.class)
    public void testDeleteWorkout_DBException() throws DBException {
        doThrow(new DBException("Delete failed")).when(mockWorkoutDB).deleteWorkout(anyInt());

        workoutManager.deleteWorkout(1);
    }

    @Test
    public void testGetSavedWorkouts_EmptyList() throws DBException {
        when(mockWorkoutDB.getAll()).thenReturn(Collections.emptyList());

        List<WorkoutProfile> result = workoutManager.getSavedWorkouts();

        assertTrue(result.isEmpty());
        verify(mockWorkoutDB).getAll();
    }

    @Test
    public void testWorkoutAccessExceptionChaining() {
        DBException dbEx = new DBException("Database error");
        WorkoutAccessException ex = new WorkoutAccessException("Workout error", dbEx);

        assertEquals("Workout error", ex.getMessage());
        assertEquals(dbEx, ex.getCause());
    }
}