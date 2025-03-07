package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseManagerIntegrationTest {
    private IExerciseDB exerciseStub;
    private ExerciseManager exerciseManager;

    @Before
    public void setup() {
        // Initialize before each test
        exerciseStub = PersistenceManager.getExerciseDB(true);
        exerciseManager = new ExerciseManager(true);
    }

    @Test public void testGetAll(){
        final List<Exercise> resultList;

        resultList = exerciseManager.getAll();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(exerciseStub.getAll(), resultList);
    }

    @Test
    public void testGetExerciseByID(){
        final Exercise result;
        final Exercise expected;

        expected = exerciseStub.getExerciseByID(0);
        result = exerciseManager.getExerciseByID(0);

        assertNotNull(result);
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getID(), result.getID());
        assertEquals(expected.getImagePath(), result.getImagePath());
        assertEquals(expected.getTags(), result.getTags());
        assertEquals(expected.getInstructions(), result.getInstructions());
    }

    @Test
    public void testInvalidGetExerciseByID(){
        final Exercise result;

        result = exerciseManager.getExerciseByID(-1);
        assertNull(result);
    }

    @Test
    public void testSearch(){
        final List<Exercise> result;
        final List<Exercise> expected = new ArrayList<>();
        final String searchString;

        expected.add(exerciseStub.getExerciseByID(0));
        searchString = expected.get(0).getName();

        result = exerciseManager.search(searchString);
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0), result.get(0));
    }

    @Test
    public void testInvalidSearch(){
        final List<Exercise> result;
        final String searchString = "12345";

        result = exerciseManager.search(searchString);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullSearch(){
        final List<Exercise> result;

        result = exerciseManager.search(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

