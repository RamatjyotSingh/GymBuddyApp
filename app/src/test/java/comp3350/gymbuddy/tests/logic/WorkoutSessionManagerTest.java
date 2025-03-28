package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Calendar;

import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.logic.exception.WorkoutSessionAccessException;

public class WorkoutSessionManagerTest {
    private IWorkoutSessionDB workoutSessionStub;
    private WorkoutSessionManager workoutSessionManager;

    //Add a failing stub for exception testing
    private IWorkoutSessionDB failingStub = new IWorkoutSessionDB() {
        @Override
        public void close() throws Exception {

        }

        @Override
        public List<WorkoutSession> getAll() throws DBException {
            throw new DBException("Test DB Failure");
        }

        @Override
        public WorkoutSession getWorkoutSessionByid(int id) throws DBException {
            throw new DBException("Test DB Failure");
        }

        @Override
        public boolean insertSession(WorkoutSession newSession) {
            return false;
        }

        @Override
        public boolean addExerciseToSession(int sessionId, WorkoutItem item) throws DBException {
            return false;
        }

        @Override
        public List<WorkoutSession> search(String searchString) throws DBException {
            throw new DBException("Test DB Failure");
        }

        @Override
        public List<WorkoutItem> getExercisesForSession(int sessionId) throws DBException {
            return Collections.emptyList();
        }

        @Override
        public boolean updateSession(WorkoutSession updatedSession) {
            return false;
        }

        @Override
        public boolean updateSessionEndTime(int sessionId, long endTime) throws DBException {
            return false;
        }

        @Override
        public boolean deleteSession(int sessionId) throws DBException {
            return false;
        }

        @Override
        public boolean removeExerciseFromSession(int sessionId, int exerciseId) throws DBException {
            return false;
        }

    };

        @Before
        public void setup() {
            // Get a stub database implementation
            workoutSessionStub = PersistenceManager.getWorkoutSessionDB(false);

            // Create workout session manager with the stub database
            workoutSessionManager = new WorkoutSessionManager(workoutSessionStub);
        }

        @Test
        public void testGetAll() {
            // Get all sessions from the stub
            List<WorkoutSession> expected = workoutSessionStub.getAll();

            // Get all sessions through the manager
            List<WorkoutSession> result = workoutSessionManager.getAll();

            // Verify results
            assertNotNull(result);
            assertEquals(expected.size(), result.size());

            // Check that all expected sessions are in the result
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).getId(), result.get(i).getId());
                assertEquals(expected.get(i).getDate(), result.get(i).getDate());
                assertEquals(expected.get(i).getWorkoutProfile().getID(),
                        result.get(i).getWorkoutProfile().getID());
            }
        }

        @Test
        public void testGetWorkoutSessionByID() {
            // Get a session from the stub
            WorkoutSession expected = workoutSessionStub.getWorkoutSessionByid(0);

            // Get the session through the manager
            WorkoutSession result = workoutSessionManager.getWorkoutSessionByID(0);

            // Verify result
            assertNotNull(result);
            assertEquals(expected.getId(), result.getId());
            assertEquals(expected.getDate(), result.getDate());
            assertEquals(expected.getWorkoutProfile().getID(), result.getWorkoutProfile().getID());
        }

        @Test(expected = Exception.class)
        public void testInvalidGetWorkoutSessionByID() {
            // This should throw an exception since the WorkoutSessionManager throws an exception
            // when a session is not found
            workoutSessionManager.getWorkoutSessionByID(-1);
        }

        @Test
        public void testSearch() {
            // Get session to search for
            WorkoutSession sessionToFind = workoutSessionStub.getWorkoutSessionByid(0);
            String searchString = sessionToFind.getWorkoutProfile().getName();

            // Perform search
            List<WorkoutSession> result = workoutSessionManager.search(searchString);

            // Verify results
            assertNotNull(result);
            assertFalse(result.isEmpty());

            // Check that the search result contains the expected session
            boolean found = false;
            for (WorkoutSession session : result) {
                if (session.getId() == sessionToFind.getId()) {
                    found = true;
                    assertEquals(sessionToFind.getDate(), session.getDate());
                    assertEquals(sessionToFind.getWorkoutProfile().getID(),
                            session.getWorkoutProfile().getID());
                    break;
                }
            }
            assertTrue("Expected session not found in search results", found);
        }

        @Test
        public void testSearchByDate() {
            // Get session to search for
            WorkoutSession sessionToFind = workoutSessionStub.getWorkoutSessionByid(0);
            String searchString = sessionToFind.getDate();

            // Perform search
            List<WorkoutSession> result = workoutSessionManager.search(searchString);

            // Verify results
            assertNotNull(result);
            assertFalse(result.isEmpty());

            // Check that the search result contains the expected session
            boolean found = false;
            for (WorkoutSession session : result) {
                if (session.getId() == sessionToFind.getId()) {
                    found = true;
                    break;
                }
            }
            assertTrue("Expected session not found in date search results", found);
        }

        @Test
        public void testInvalidSearch() {
            // Search for non-existent term
            List<WorkoutSession> result = workoutSessionManager.search("NonExistentSession12345");

            // Verify results
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        public void testNullSearch() {
            // Search with null term
            List<WorkoutSession> result = workoutSessionManager.search(null);

            // Verify results
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        public void testSaveSession() {
            // Get a sample workout profile from an existing session to use in the new session
            WorkoutSession existingSession = workoutSessionStub.getWorkoutSessionByid(0);

            // Get current time
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();

            // Create a new session with the correct constructor parameters
            WorkoutSession newSession = new WorkoutSession(
                    100, // A unique ID for the test
                    currentTime, // Start time
                    currentTime + 3600000, // End time (1 hour later)
                    new ArrayList<>(existingSession.getSessionItems()), // Session items
                    existingSession.getWorkoutProfile() // Profile
            );

            // Save the session
            boolean result = workoutSessionManager.saveSession(newSession);

            // Verify save was successful
            assertTrue(result);

            // Get all sessions to find the newly added one
            List<WorkoutSession> allSessions = workoutSessionManager.getAll();

            // Find the added session - either by searching by ID if the DB uses our ID
            // or by matching profile and date
            WorkoutSession addedSession = null;

            // Try to find by matching both the profile and approximate time
            for (WorkoutSession session : allSessions) {
                if (session.getWorkoutProfile().getID() == newSession.getWorkoutProfile().getID() &&
                        Math.abs(session.getStartTime() - newSession.getStartTime()) < 1000) { // Within 1 second
                    addedSession = session;
                    break;
                }
            }

            // Verify the added session has the correct data
            assertNotNull("Added session not found", addedSession);
            assertEquals(newSession.getWorkoutProfile().getID(), addedSession.getWorkoutProfile().getID());
            assertEquals(newSession.getSessionItems().size(), addedSession.getSessionItems().size());

            // The date string might be formatted differently, so compare time values
            long timeDifference = Math.abs(newSession.getStartTime() - addedSession.getStartTime());
            assertTrue("Start time should be within 1 second", timeDifference < 1000);
        }


        //Testing exceptions
        @Test(expected = WorkoutSessionAccessException.class)
        public void testGetAllThrowsWhenDBFails() {
            WorkoutSessionManager failingManager = new WorkoutSessionManager(failingStub);
            failingManager.getAll();
        }

        @Test(expected = WorkoutSessionAccessException.class)
        public void testGetSessionByIDThrowsWhenDBFails() {
            WorkoutSessionManager failingManager = new WorkoutSessionManager(failingStub);
            failingManager.getWorkoutSessionByID(1);
        }

        @Test
        public void testSearchThrowsWithCorrectMessageWhenDBFails() {
            WorkoutSessionManager failingManager = new WorkoutSessionManager(failingStub);

            try {
                failingManager.search("test");
                fail("Expected WorkoutSessionAccessException");
            } catch (WorkoutSessionAccessException e) {
                assertEquals("Failed to search workout sessions", e.getMessage());
                assertTrue(e.getCause() instanceof DBException);
                assertEquals("Test DB Failure", e.getCause().getMessage());
            }
        }
}

