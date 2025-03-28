package comp3350.gymbuddy.tests.logic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseManagerTest {
    private IExerciseDB exerciseStub;
    private ExerciseManager exerciseManager;

    @Before
    public void setup() {
        // Get a stub database implementation
        exerciseStub = PersistenceManager.getExerciseDB(false);
        
        // Create exercise manager with the stub database
        exerciseManager = new ExerciseManager(exerciseStub);
    }

    @Test
    public void testGetAll() {
        final List<Exercise> resultList;
        final List<Exercise> expectedList = exerciseStub.getAll();

        resultList = exerciseManager.getAll();
        
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(expectedList.size(), resultList.size());
        
        // Check that the lists contain the same elements
        for (Exercise expected : expectedList) {
            boolean found = false;
            for (Exercise result : resultList) {
                if (expected.getID() == result.getID()) {
                    found = true;
                    assertEquals(expected.getName(), result.getName());
                    assertEquals(expected.getImagePath(), result.getImagePath());
                    break;
                }
            }
            assertTrue("Exercise with ID " + expected.getID() + " not found in result", found);
        }
    }

    @Test
    public void testGetExerciseByID() {
        final Exercise expected = exerciseStub.getExerciseByID(0);
        final Exercise result = exerciseManager.getExerciseByID(0);

        assertNotNull(result);
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getID(), result.getID());
        assertEquals(expected.getImagePath(), result.getImagePath());
        assertEquals(expected.getInstructions(), result.getInstructions());
        assertEquals(expected.isTimeBased(), result.isTimeBased());
        assertEquals(expected.hasWeight(), result.hasWeight());
    }

    @Test(expected = Exception.class)
    public void testInvalidGetExerciseByID() {
        // This should throw an exception since the ExerciseManager throws an exception
        // when an exercise is not found
        exerciseManager.getExerciseByID(-1);
    }

    @Test
    public void testSearch() {
        final Exercise expected = exerciseStub.getExerciseByID(0);
        final String searchString = expected.getName();

        final List<Exercise> result = exerciseManager.search(searchString);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Verify the search result contains the expected exercise
        boolean found = false;
        for (Exercise exercise : result) {
            if (exercise.getID() == expected.getID()) {
                found = true;
                assertEquals(expected.getName(), exercise.getName());
                break;
            }
        }
        assertTrue("Expected exercise not found in search results", found);
    }

    @Test
    public void testInvalidSearch() {
        final List<Exercise> result = exerciseManager.search("NonExistentExercise12345");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullSearch() {
        final List<Exercise> result = exerciseManager.search(null);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

