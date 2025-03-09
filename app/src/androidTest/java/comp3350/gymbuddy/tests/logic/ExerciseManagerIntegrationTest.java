package comp3350.gymbuddy.tests.logic;


import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseManagerIntegrationTest {
    private ExerciseManager exerciseManager;
    private Context context;

    @Before
    public void setUp() throws SQLException {
        // Get app context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Enable test mode for HSQLDB
        HSQLDBHelper.setTestMode(true);
        HSQLDBHelper.resetTestDatabase(context); // Ensure clean database

        // Ensure HSQLDB initializes before tests
        try (Connection conn = HSQLDBHelper.getConnection()) {
            assertNotNull("Database connection should not be null", conn);
        }

        // Initialize ExerciseManager with test DB
        exerciseManager = new ExerciseManager(true);
    }

    @Test
    public void testGetAllExercises() {
        List<Exercise> exercises = exerciseManager.getAll();
        assertNotNull("Exercise list should not be null", exercises);
        assertFalse("Exercise list should not be empty", exercises.isEmpty());
    }

    @Test
    public void testGetExerciseByID() {
        int testID = 1;
        Exercise exercise = exerciseManager.getExerciseByID(testID);
        assertNotNull("Exercise should exist", exercise);
        assertEquals("Exercise ID should match", testID, exercise.getID());
    }

    @Test
    public void testSearchExercises() {
        String keyword = "Push-up";
        List<Exercise> results = exerciseManager.search(keyword);
        assertNotNull("Search results should not be null", results);
        assertFalse("Search results should not be empty", results.isEmpty());
        boolean found = results.stream().anyMatch(exercise ->
                exercise.getName().toLowerCase().contains(keyword.toLowerCase()));
        assertTrue("At least one result should match the search keyword", found);
    }

    @After
    public void tearDown() {
        HSQLDBHelper.resetTestDatabase(context);
    }
}
