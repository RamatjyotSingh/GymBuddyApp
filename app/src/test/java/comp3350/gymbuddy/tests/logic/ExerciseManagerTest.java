package comp3350.gymbuddy.tests.logic;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;

public class ExerciseManagerTest {
    private ExerciseManager exerciseManager;
    private List<Exercise> existingExercises;

    @Before
    public void setup() {
        exerciseManager = new ExerciseManager(false);
        existingExercises = exerciseManager.getAll();
    }

    @Test
    public void testGetAll() {
        List<Exercise> result = exerciseManager.getAll();
        assertNotNull(result);
        assertFalse("Database should contain some exercises by default", result.isEmpty());
    }

    @Test
    public void testGetExerciseByID() {
        //Get first exercise from the database
        Exercise firstExercise = existingExercises.get(0);
        Exercise result = exerciseManager.getExerciseByID(firstExercise.getID());

        assertNotNull(result);
        assertEquals(firstExercise.getName(), result.getName());
        assertEquals(firstExercise.getID(), result.getID());
        assertEquals(firstExercise.getInstructions(), result.getInstructions());
    }

    @Test
    public void testInvalidGetExerciseByID() {
        Exercise result = exerciseManager.getExerciseByID(-1);
        assertNull(result);
    }

    @Test
    public void testSearch() {
        //Use the name of the first existing exercise
        String searchTerm = existingExercises.get(0).getName();
        List<Exercise> result = exerciseManager.search(searchTerm);

        assertNotNull(result);
        assertFalse("Should find at least one matching exercise", result.isEmpty());
        assertTrue("Result should contain the exercise we searched for",
                result.stream().anyMatch(e -> e.getName().equalsIgnoreCase(searchTerm)));
    }

    @Test
    public void testSearch_PartialMatch() {
        //Use part of an exercise name
        String partialName = existingExercises.get(0).getName().substring(0, 3);
        List<Exercise> result = exerciseManager.search(partialName);

        assertNotNull(result);
        assertFalse("Should find matches for partial names", result.isEmpty());
    }

    @Test
    public void testSearch_CaseInsensitive() {
        //Search with different casing
        String searchTerm = existingExercises.get(0).getName().toUpperCase();
        List<Exercise> result = exerciseManager.search(searchTerm);

        assertNotNull(result);
        assertFalse("Search should be case insensitive", result.isEmpty());
    }

    @Test
    public void testInvalidSearch() {
        List<Exercise> result = exerciseManager.search("NonexistentExerciseName123");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullSearch() {
        List<Exercise> result = exerciseManager.search(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptySearch() {
        List<Exercise> result = exerciseManager.search("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

