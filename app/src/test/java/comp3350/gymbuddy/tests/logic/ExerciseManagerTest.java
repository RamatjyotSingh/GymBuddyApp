package comp3350.gymbuddy.tests.logic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseManagerTest {
    private ExerciseManager exerciseManager;
    private IExerciseDB exercisePersistence;

    @Before
    public void setup() {
        // Initialize before each test
        exercisePersistence = mock(IExerciseDB.class);
        exerciseManager = new ExerciseManager(false);
    }

    @Test public void testGetAllExercises(){
        final List<Exercise> exerciseList = new ArrayList<>();
        final List<Exercise> resultList;

        final Exercise exercise = new Exercise(0, "Push-up", null, null, null,false,false);
        exerciseList.add(exercise);

        // define the behaviour of the mock inside the logic object when this method is called with these arguments
        when(exercisePersistence.getAll()).thenReturn(exerciseList);

        resultList = exerciseManager.getAll();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(exercise, resultList.get(0));

        verify(exercisePersistence).getAll();
    }

    @Test
    public void testGetExerciseByID(){
        final Exercise result;

        when(exercisePersistence.getExerciseByID(0)).thenReturn(new Exercise(0,"Push-up", null, null, null, false,false));

        result = exerciseManager.getExerciseByID(0);
        assertNotNull(result);
        assertTrue(result.getName().equalsIgnoreCase("Push-up"));

        verify(exercisePersistence).getExerciseByID(0);
    }
}

