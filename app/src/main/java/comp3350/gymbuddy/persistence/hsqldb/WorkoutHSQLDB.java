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

/**
 * WorkoutHSQLDB implements IWorkoutDB, providing database operations for workout profiles and items.
 */
public class WorkoutHSQLDB implements IWorkoutDB {

    /**
     * Retrieves all workout profiles from the database.
     * @return A list of all workout profiles.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    @NonNull
    public List<WorkoutProfile> getAll() throws DBException {
        List<WorkoutProfile> workoutProfiles = new ArrayList<>();
        String query = "SELECT * FROM workout_profile";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and extract workout profiles
            while (rs.next()) {
                workoutProfiles.add(extractWorkoutProfile(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workouts.");
        }

        return workoutProfiles;
    }

    /**
     * Saves a new workout profile to the database.
     * @param profile The workout profile to save.
     * @throws DBException If an error occurs while saving the profile.
     */
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

    /**
     * Retrieves a workout profile by its ID.
     * @param id The ID of the workout profile to retrieve.
     * @return The corresponding WorkoutProfile object, or null if not found.
     * @throws DBException If an error occurs while accessing the database.
     */
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

                    // Fetch associated workout items
                    List<WorkoutItem> workoutItems = getWorkoutItemsByProfileId(id);

                    workoutProfile = new WorkoutProfile(profile_id, name, iconPath, workoutItems);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load workout profile.");
        }

        return workoutProfile;
    }

    /**
     * Extracts workout profile data from a ResultSet and constructs a WorkoutProfile object.
     * @param rs The ResultSet containing the workout profile data.
     * @return A new WorkoutProfile object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private WorkoutProfile extractWorkoutProfile(ResultSet rs) throws SQLException {
        int id = rs.getInt("profile_id");
        String name = rs.getString("profile_name");
        String iconPath = rs.getString("icon_path");

        // Fetch associated workout items
        List<WorkoutItem> workoutItems = getWorkoutItemsByProfileId(id);

        return new WorkoutProfile(id, name, iconPath, workoutItems);
    }

    /**
     * Extracts workout item data from a ResultSet and constructs a WorkoutItem object.
     * @param rs The ResultSet containing the workout item data.
     * @return A new WorkoutItem object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private WorkoutItem extractWorkoutItem(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        int sets = rs.getInt("sets");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("duration");

        // Retrieve the corresponding exercise
        Exercise exercise = PersistenceManager.getExerciseDB(true).getExerciseByID(exerciseId);

        return new WorkoutItem(exercise, sets, reps, weight, time);
    }

    /**
     * Retrieves a list of workout items associated with a given workout profile ID.
     * @param id The ID of the workout profile whose items need to be fetched.
     * @return A list of associated WorkoutItem objects.
     * @throws DBException If an error occurs while accessing the database.
     */
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
