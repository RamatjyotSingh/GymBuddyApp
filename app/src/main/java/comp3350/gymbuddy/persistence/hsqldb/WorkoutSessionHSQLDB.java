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

public class WorkoutSessionHSQLDB implements IWorkoutSessionDB {
    @Override
    @NonNull
    public List<WorkoutSession> getAll() throws DBException {
        List<WorkoutSession> workoutSessions = new ArrayList<>();

        String query = "SELECT * FROM workout_session";

        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                workoutSessions.add(extractWorkoutSession(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout sessions.");
        }

        return workoutSessions;
    }

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

    @NonNull
    private WorkoutSession extractWorkoutSession(ResultSet rs) throws SQLException {
        int sessionId = rs.getInt("session_id");
        long startTime = rs.getLong("start_time");
        long endTime = rs.getLong("end_time");
        int profileId = rs.getInt("profile_id");

        // Fetch associated WorkoutProfile
        WorkoutProfile profile = PersistenceManager.getWorkoutDB(true).getWorkoutProfileById(profileId);

        // Fetch all session items related to this session
        List<WorkoutItem> workoutItems = getWorkoutItemsBySessionId(sessionId);

        return new WorkoutSession(sessionId, startTime, endTime, workoutItems, profile);
    }

    @NonNull
    private WorkoutItem extractSessionItem(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("duration");

        Exercise exercise = PersistenceManager.getExerciseDB(true).getExerciseByID(exerciseId);

        return new WorkoutItem(exercise, 1, reps, weight, time); // Each item represents only one set.
    }

    @NonNull
    private List<WorkoutItem> getWorkoutItemsBySessionId(int id) throws DBException {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        String query = "SELECT * FROM session_item WHERE profile_id = ?";

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
