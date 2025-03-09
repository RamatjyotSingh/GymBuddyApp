package comp3350.gymbuddy.persistence.hsqldb;

import androidx.annotation.NonNull;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutSessionHSQLDB implements IWorkoutSessionDB, providing database operations for workout sessions.
 */
public class WorkoutSessionHSQLDB implements IWorkoutSessionDB {

    /**
     * Retrieves all workout sessions from the database.
     * @return A list of all workout sessions.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    @NonNull
    public List<WorkoutSession> getAll() throws DBException {
        List<WorkoutSession> workoutSessions = new ArrayList<>();
        String query = "SELECT * FROM workout_session";

        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set and extract workout sessions
            while (rs.next()) {
                workoutSessions.add(extractWorkoutSession(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout sessions.");
        }

        return workoutSessions;
    }

    /**
     * Retrieves a specific workout session by its ID.
     * @param id The ID of the workout session to retrieve.
     * @return The corresponding WorkoutSession object, or null if not found.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public WorkoutSession getWorkoutSessionByid(int id) throws DBException {
        WorkoutSession workoutSession = null;
        String query = "SELECT * FROM workout_session WHERE session_id = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    workoutSession = extractWorkoutSession(rs);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout session");
        }

        return workoutSession;
    }

    /**
     * Extracts workout session data from a ResultSet and constructs a WorkoutSession object.
     * @param rs The ResultSet containing the workout session data.
     * @return A new WorkoutSession object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private WorkoutSession extractWorkoutSession(ResultSet rs) throws SQLException {
        int sessionId = rs.getInt("session_id");
        long startTime = rs.getLong("start_time");
        long endTime = rs.getLong("end_time");
        int profileId = rs.getInt("profile_id");

        // Fetch associated WorkoutProfile
        WorkoutProfile profile = PersistenceManager.getWorkoutDB(true).getWorkoutProfileById(profileId);

        // Fetch all workout items related to this session
        List<WorkoutItem> workoutItems = getWorkoutItemsBySessionId(sessionId);

        return new WorkoutSession(sessionId, startTime, endTime, workoutItems, profile);
    }

    /**
     * Extracts session item data from a ResultSet and constructs a WorkoutItem object.
     * @param rs The ResultSet containing the session item data.
     * @return A new WorkoutItem object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private WorkoutItem extractSessionItem(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("duration");

        // Retrieve the corresponding exercise
        Exercise exercise = PersistenceManager.getExerciseDB(true).getExerciseByID(exerciseId);

        return new WorkoutItem(exercise, 1, reps, weight, time); // Each item represents only one set.
    }

    /**
     * Retrieves a list of workout items associated with a given workout session ID.
     * @param id The ID of the workout session whose items need to be fetched.
     * @return A list of associated WorkoutItem objects.
     * @throws DBException If an error occurs while accessing the database.
     */
    @NonNull
    private List<WorkoutItem> getWorkoutItemsBySessionId(int id) throws DBException {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        String query = "SELECT * FROM session_item WHERE session_id = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    workoutItems.add(extractSessionItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout exercises.");
        }

        return workoutItems;
    }
}
