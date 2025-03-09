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

import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WorkoutSessionManagerIntegrationTest {
    private WorkoutSessionManager workoutSessionManager;
    private Context context;

    @Before
    public void setUp() throws SQLException {
        // Get application context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Reset test database
        HSQLDBHelper.setTestMode(true);
        HSQLDBHelper.resetTestDatabase(context);

        // Ensure the database initializes properly
        try (Connection conn = HSQLDBHelper.getConnection()) {
            assertNotNull("Database connection should not be null", conn);
        }

        // Initialize WorkoutSessionManager with test DB
        workoutSessionManager = new WorkoutSessionManager(true);
    }

    @Test
    public void testRetrieveAllWorkoutSessions() {
        // Act: Retrieve all workout sessions
        List<WorkoutSession> workoutSessions = workoutSessionManager.getAll();

        // Assert: Ensure the list is not null and contains sessions
        assertNotNull("Workout session list should not be null", workoutSessions);
        assertFalse("Workout session list should not be empty", workoutSessions.isEmpty());

        // Ensure at least one session is present
        boolean found = workoutSessions.stream()
                .anyMatch(session -> session.getWorkoutProfile() != null);

        assertTrue("At least one session should have a valid workout profile", found);
    }

    @Test
    public void testRetrieveWorkoutSessionById() {
        // Arrange: Assuming a session with ID 1 exists in the DB
        int testID = 1;

        // Act: Retrieve session by ID
        WorkoutSession session = workoutSessionManager.getWorkoutSessionByID(testID);

        // Assert: Ensure session is found and has valid data
        assertNotNull("Workout session should exist", session);
        assertEquals("Workout session ID should match", testID, session.getId());
        assertNotNull("Workout session profile should not be null", session.getWorkoutProfile());
    }

    @Test
    public void testSearchWorkoutSessions() {
        // Arrange: Searching for a known profile name
        String searchKeyword = "Full Body Workout"; // Ensure this exists in the test data

        // Act: Perform search
        List<WorkoutSession> results = workoutSessionManager.search(searchKeyword);

        // Assert: Search results should not be null or empty
        assertNotNull("Search results should not be null", results);
        assertFalse("Search results should not be empty", results.isEmpty());

        // Ensure at least one result matches the search
        boolean found = results.stream().anyMatch(session ->
                session.getWorkoutProfile().getName().toLowerCase().contains(searchKeyword.toLowerCase()));

        assertTrue("At least one session should match the search keyword", found);
    }

    @After
    public void tearDown() {
        // Cleanup the test database after each test
        HSQLDBHelper.resetTestDatabase(context);
    }
}

