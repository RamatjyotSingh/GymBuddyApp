package comp3350.gymbuddy.logic.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.logic.exception.WorkoutSessionAccessException;
import timber.log.Timber;

/**
 * Manager class for WorkoutSession-related operations
 */
public class WorkoutSessionManager {
    private final IWorkoutSessionDB workoutSessionDB;
    private static final String TAG = "WorkoutSessionManager";

    /**
     * Creates a WorkoutSessionManager with the specified database implementation
     * @param workoutSessionDB The workout session database to use
     */
    public WorkoutSessionManager(IWorkoutSessionDB workoutSessionDB) {
        this.workoutSessionDB = workoutSessionDB;
    }


    /**
     * Get all workout sessions
     * @return List of workout sessions
     * @throws WorkoutSessionAccessException if database access fails
     */
    public List<WorkoutSession> getAll() {
        try {
            return Collections.unmodifiableList(workoutSessionDB.getAll());
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve workout sessions");
            throw new WorkoutSessionAccessException("Failed to retrieve workout sessions", e);
        }
    }

    /**
     * Get workout session by ID
     * @param id Session ID
     * @return Workout session
     * @throws WorkoutSessionAccessException if session not found or database access fails
     */
    public WorkoutSession getWorkoutSessionByID(int id) {
        try {
            WorkoutSession session = workoutSessionDB.getWorkoutSessionByid(id);
            if (session == null) {
                throw new WorkoutSessionAccessException("Workout session not found with ID: " + id);
            }
            return session;
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve workout session with ID %d", id);
            throw new WorkoutSessionAccessException("Failed to retrieve workout session with ID: " + id, e);
        }
    }

    /**
     * Search workout sessions by date or workout name
     * @param searchString Search term
     * @return List of matching sessions
     * @throws WorkoutSessionAccessException if database access fails
     */
    public List<WorkoutSession> search(String searchString) {
        List<WorkoutSession> results = new ArrayList<>();

        try {
            if (searchString != null && !searchString.isEmpty()) {
                for (var session : workoutSessionDB.getAll()) {
                    if (session.getDate().toLowerCase().contains(searchString.toLowerCase()) ||
                        session.getWorkoutProfile().getName().toLowerCase().contains(searchString.toLowerCase())) {
                        results.add(session);
                    }
                }
            }
            return results;
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to search workout sessions");
            throw new WorkoutSessionAccessException("Failed to search workout sessions", e);
        }
    }

    public boolean saveSession(WorkoutSession newSession) {
       return workoutSessionDB.insertSession(newSession);
    }
}
