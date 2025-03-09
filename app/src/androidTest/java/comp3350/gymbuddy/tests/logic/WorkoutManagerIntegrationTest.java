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
import java.sql.Statement;
import java.util.List;

import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WorkoutManagerIntegrationTest {
    private WorkoutManager workoutManager;
    private Context context;

    @Before
    public void setUp() throws SQLException {
        // Get application context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Reset test database
        HSQLDBHelper.setTestMode(true);
        HSQLDBHelper.resetTestDatabase(context);

        // Delete existing workout profiles before inserting new ones
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM workout_profile");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear workout profiles before tests: " + e.getMessage());
        }

        // Ensure database initializes properly
        try (Connection conn = HSQLDBHelper.getConnection()) {
            assertNotNull("Database connection should not be null", conn);
        }

        // Initialize WorkoutManager with test DB
        workoutManager = new WorkoutManager(true);
    }


    @Test
    public void testSaveWorkoutProfile() {
        // Arrange: Create a workout profile
        WorkoutProfile profile = new WorkoutProfile(1, "Strength Training", null, List.of());

        // Act: Save the profile to the database
        workoutManager.saveWorkout(profile);

        // Assert: Check if the workout profile is saved successfully
        List<WorkoutProfile> workoutProfiles = workoutManager.getAll();
        assertNotNull("Workout profile list should not be null", workoutProfiles);
        assertFalse("Workout profile list should not be empty", workoutProfiles.isEmpty());

        // Ensure at least one profile was inserted
        boolean found = workoutProfiles.stream()
                .anyMatch(p -> p.getName().equals("Strength Training"));

        assertTrue("Workout profile 'Strength Training' should be in the database", found);
    }

    @Test
    public void testRetrieveWorkoutProfiles() {
        // Arrange: Ensure at least one profile exists before retrieval
        WorkoutProfile profile1 = new WorkoutProfile(1, "Strength Training", null, List.of());
        WorkoutProfile profile2 = new WorkoutProfile(2, "Cardio", null, List.of());

        workoutManager.saveWorkout(profile1);
        workoutManager.saveWorkout(profile2);

        // Act: Retrieve all workout profiles from the database
        List<WorkoutProfile> workoutProfiles = workoutManager.getAll();

        // Assert: Ensure retrieval works correctly
        assertNotNull("Workout profile list should not be null", workoutProfiles);
        assertEquals("There should be 2 workout profiles in the database", 2, workoutProfiles.size());

        // Ensure correct profiles were inserted and retrieved
        assertEquals("First profile name should be 'Strength Training'", "Strength Training", workoutProfiles.get(0).getName());
        assertEquals("Second profile name should be 'Cardio'", "Cardio", workoutProfiles.get(1).getName());
    }

    @After
    public void tearDown() {
        // Cleanup the test database after each test
        HSQLDBHelper.resetTestDatabase(context);
    }
}

