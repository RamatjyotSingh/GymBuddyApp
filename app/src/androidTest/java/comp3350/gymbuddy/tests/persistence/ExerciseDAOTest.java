package comp3350.gymbuddy.tests.persistence;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ExerciseDAOTest {
    private static HSQLDBTestHelper dbHelper;
    private IExerciseDB exerciseDB;
    
    @Before
    public void setUp() throws Exception {
        dbHelper = new HSQLDBTestHelper();
        dbHelper.setUp();
        exerciseDB = dbHelper.getFactory().createExerciseDB();
    }
    
    @After
    public void tearDown() throws Exception {
        dbHelper.tearDown();
    }
    
    @Test
    public void testGetAll() throws DBException {
        // Get all exercises
        List<Exercise> exercises = exerciseDB.getAll();
        
        // Verify results
        assertNotNull("Exercise list should not be null", exercises);
        assertFalse("Exercise list should not be empty", exercises.isEmpty());
        
        // Based on our test data, we expect exactly 3 exercises
        assertEquals("Should have exactly 3 exercises", 3, exercises.size());
        
        // Verify specific exercises
        boolean foundPushup = false;
        boolean foundSquat = false;
        boolean foundPlank = false;
        
        for (Exercise exercise : exercises) {
            if (exercise.getName().equals("Push-Up")) {
                foundPushup = true;
                assertFalse("Push-Up should not be time-based", exercise.isTimeBased());
                assertFalse("Push-Up should not have weight", exercise.hasWeight());
                
                // Check tags
                List<Tag> tags = exercise.getTags();
                assertNotNull("Tags should not be null", tags);
                assertFalse("Tags should not be empty", tags.isEmpty());
                
                // Check for the correct tags based on our test data
                boolean hasUpperBody = false;
                boolean hasBeginner = false;
                
                for (Tag tag : tags) {
                    if (tag.getName().equals("Upper Body")) hasUpperBody = true;
                    if (tag.getName().equals("Beginner")) hasBeginner = true;
                }
                
                assertTrue("Push-Up should have Upper Body tag", hasUpperBody);
                assertTrue("Push-Up should have Beginner tag", hasBeginner);
                assertEquals("Push-Up should have exactly 2 tags", 2, tags.size());
                
            } else if (exercise.getName().equals("Squat")) {
                foundSquat = true;
                assertFalse("Squat should not be time-based", exercise.isTimeBased());
                assertTrue("Squat should have weight", exercise.hasWeight());
                
                // Check tags
                List<Tag> tags = exercise.getTags();
                assertNotNull("Tags should not be null", tags);
                assertFalse("Tags should not be empty", tags.isEmpty());
                
                // Check for the Lower Body tag
                boolean hasLowerBody = false;
                for (Tag tag : tags) {
                    if (tag.getName().equals("Lower Body")) hasLowerBody = true;
                }
                
                assertTrue("Squat should have Lower Body tag", hasLowerBody);
                assertEquals("Squat should have exactly 1 tag", 1, tags.size());
                
            } else if (exercise.getName().equals("Plank")) {
                foundPlank = true;
                assertTrue("Plank should be time-based", exercise.isTimeBased());
                assertFalse("Plank should not have weight", exercise.hasWeight());
                
                // Check tags
                List<Tag> tags = exercise.getTags();
                assertNotNull("Tags should not be null", tags);
                assertFalse("Tags should not be empty", tags.isEmpty());
                
                // Check for the Core tag
                boolean hasCore = false;
                for (Tag tag : tags) {
                    if (tag.getName().equals("Core")) hasCore = true;
                }
                
                assertTrue("Plank should have Core tag", hasCore);
                assertEquals("Plank should have exactly 1 tag", 1, tags.size());
            }
        }
        
        assertTrue("Push-Up exercise should exist", foundPushup);
        assertTrue("Squat exercise should exist", foundSquat);
        assertTrue("Plank exercise should exist", foundPlank);
    }
    
    @Test
    public void testGetExerciseById() throws DBException {
        // Get exercise by ID
        Exercise exercise = exerciseDB.getExerciseByID(1);
        
        // Verify result
        assertNotNull("Exercise should not be null", exercise);
        assertEquals("Exercise ID should be 1", 1, exercise.getID());
        assertEquals("Exercise name should be Push-Up", "Push-Up", exercise.getName());
        
        // Verify another exercise
        Exercise squatExercise = exerciseDB.getExerciseByID(2);
        assertNotNull("Squat exercise should not be null", squatExercise);
        assertEquals("Exercise ID should be 2", 2, squatExercise.getID());
        assertEquals("Exercise name should be Squat", "Squat", squatExercise.getName());
        
        // Verify non-existent exercise returns null
        Exercise nonExistentExercise = exerciseDB.getExerciseByID(999);
        assertEquals("Non-existent exercise should return null", null, nonExistentExercise);
    }
}