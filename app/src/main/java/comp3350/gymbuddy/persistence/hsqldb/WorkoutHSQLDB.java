package comp3350.gymbuddy.persistence.hsqldb;

import androidx.annotation.NonNull;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHSQLDB implements IWorkoutDB {
    @Override
    @NonNull
    public List<WorkoutProfile> getAll() throws DBException {
        List<WorkoutProfile> workoutProfiles = new ArrayList<>();

        String query = "SELECT * FROM workout_profile";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                workoutProfiles.add(extractWorkoutProfile(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workouts.");
        }

        return workoutProfiles;
    }

    @Override
    public void saveWorkout(WorkoutProfile profile) throws DBException {
        String query = "INSERT INTO workout_profile (profile_name, icon_path) VALUES (?, ?)";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, profile.getName());
            stmt.setString(2, profile.getIconPath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Failed to save workout.");
        }
    }

    @Override
    public WorkoutProfile getWorkoutProfileById(int id) throws DBException {
        WorkoutProfile workoutProfile = null;

        String query = "SELECT * FROM workout_profile WHERE id = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int profile_id = rs.getInt("profile_id");
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");

                    List<WorkoutItem> workoutItems = getWorkoutItemsByProfileId(id);

                    workoutProfile = new WorkoutProfile(profile_id, name, iconPath, workoutItems);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout profile.");
        }

        return workoutProfile;
    }

    @NonNull
    private WorkoutProfile extractWorkoutProfile(ResultSet rs) throws SQLException {
        int id = rs.getInt("profile_id");
        String name = rs.getString("profile_name");
        String iconPath = rs.getString("icon_path");

        List<WorkoutItem> workoutItems = getWorkoutItemsByProfileId(id);

        return new WorkoutProfile(id, name, iconPath, workoutItems);
    }

    @NonNull
    private WorkoutItem extractWorkoutItem(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        int sets = rs.getInt("sets");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("duration");

        Exercise exercise = PersistenceManager.getExerciseDB(true).getExerciseByID(exerciseId);

        return new WorkoutItem(exercise, sets, reps, weight, time);
    }

    @NonNull
    private List<WorkoutItem> getWorkoutItemsByProfileId(int id) throws DBException {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        String query = "SELECT * FROM workout_item WHERE profile_id = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    workoutItems.add(extractWorkoutItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout exercises.");
        }

        return workoutItems;
    }
}
