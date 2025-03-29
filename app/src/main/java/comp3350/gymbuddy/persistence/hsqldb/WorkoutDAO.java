package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import timber.log.Timber;

public class WorkoutDAO implements IWorkoutDB {
    private static final String TAG = "WorkoutDAO";
    private final Connection connection;
    private final IExerciseDB exerciseDB;
    
    /**
     * Constructor with dependency injection for better architecture
     * @param connection Database connection
     * @param exerciseDB Exercise database implementation
     */
    public WorkoutDAO(Connection connection, IExerciseDB exerciseDB) {
        this.connection = connection;
        this.exerciseDB = exerciseDB;
    }

    @Override
    public void saveWorkout(WorkoutProfile profile) throws DBException {
        if (profile == null) {
            throw new DBException("Workout profile cannot be null");
        }

        try {
            // Begin transaction
            connection.setAutoCommit(false);
            
            try {
                // First, check if profile already exists
                WorkoutProfile existingProfile = null;
                int profileId;
                
                // Check by name
                try (PreparedStatement stmt = connection.prepareStatement(
                        "SELECT * FROM PUBLIC.workout_profile WHERE profile_name = ?")) {
                    stmt.setString(1, profile.getName());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            profileId = rs.getInt("profile_id");
                            existingProfile = new WorkoutProfile(
                                    profileId,
                                    rs.getString("profile_name"),
                                    rs.getString("icon_path"),
                                    new ArrayList<>(),
                                    false
                            );
                        }
                    }
                }
                
                if (existingProfile == null) {
                    // Insert new profile
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO PUBLIC.workout_profile (profile_id, profile_name, icon_path) VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        
                        // Use provided ID or generate next ID
                        profileId = profile.getID() > 0 ? profile.getID() : getNextProfileId();
                        
                        stmt.setInt(1, profileId);
                        stmt.setString(2, profile.getName());
                        stmt.setString(3, profile.getIconPath());
                        stmt.executeUpdate();
                    }
                } else {
                    // Use existing profile ID
                    profileId = existingProfile.getID();
                    
                    // Update existing profile
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "UPDATE PUBLIC.workout_profile SET icon_path = ? WHERE profile_id = ?")) {
                        stmt.setString(1, profile.getIconPath());
                        stmt.setInt(2, profileId);
                        stmt.executeUpdate();
                    }
                    
                    // Delete existing workout items to replace with new ones
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "DELETE FROM PUBLIC.profile_exercise WHERE profile_id = ?")) {
                        stmt.setInt(1, profileId);
                        stmt.executeUpdate();
                    }
                }
                
                // Now insert workout items
                if (profile.getWorkoutItems() != null && !profile.getWorkoutItems().isEmpty()) {
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO PUBLIC.profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) VALUES (?, ?, ?, ?, ?, ?)")) {
                        
                        for (WorkoutItem item : profile.getWorkoutItems()) {
                            stmt.setInt(1, profileId);
                            stmt.setInt(2, item.getExercise().getID());
                            stmt.setInt(3, item.getSets());
                            stmt.setInt(4, item.getReps());
                            stmt.setDouble(5, item.getWeight());
                            stmt.setDouble(6, item.getTime());
                            stmt.executeUpdate();
                        }
                    }
                }
                
                // Commit transaction
                connection.commit();

            } catch (SQLException e) {
                // Rollback transaction on error
                connection.rollback();
                throw new DBException("Error saving workout profile: " + e.getMessage(), e);
            } finally {
                // Restore auto-commit
                connection.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            throw new DBException("Error connecting to database: " + e.getMessage(), e);
        }
    }

    // Helper method to get next available profile ID
    private int getNextProfileId() throws SQLException {
        int nextId = 1;
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT MAX(profile_id) AS max_id FROM PUBLIC.workout_profile")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nextId = rs.getInt("max_id") + 1;
                }
            }
        }
        
        return nextId;
    }
    
    @Override
    public List<WorkoutProfile> getAll() throws DBException {
        List<WorkoutProfile> profiles = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM PUBLIC.workout_profile WHERE is_deleted = 0")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("profile_id");
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    boolean isDeleted = rs.getBoolean("is_deleted");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    profiles.add(new WorkoutProfile(id, name, iconPath, items, isDeleted));
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error retrieving workout profiles: " + e.getMessage(), e);
        }
        
        return profiles;
    }
    
    @Override
    public WorkoutProfile getWorkoutProfileById(int id) throws DBException {
        WorkoutProfile profile = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM PUBLIC.workout_profile WHERE profile_id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean isDeleted = rs.getBoolean("is_deleted");
                    
                    // Only load and return if not deleted
                    if (!isDeleted) {
                        String name = rs.getString("profile_name");
                        String iconPath = rs.getString("icon_path");
                        
                        // Get workout items for this profile
                        List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                        
                        profile = new WorkoutProfile(id, name, iconPath, items, false);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error retrieving workout profile: " + e.getMessage(), e);
        }
        
        return profile;
    }
    
    @Override
    public WorkoutProfile getWorkoutProfileByIdIncludingDeleted(int id) throws DBException {
        WorkoutProfile profile = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM PUBLIC.workout_profile WHERE profile_id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    boolean isDeleted = rs.getBoolean("is_deleted");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    profile = new WorkoutProfile(id, name, iconPath, items, isDeleted);
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error retrieving workout profile: " + e.getMessage(), e);
        }
        
        return profile;
    }
    
    @Override
    public void deleteWorkout(int id) throws DBException {
        // Mark the profile as deleted (This keeps it stored in the DB for the logs).
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE PUBLIC.workout_profile SET is_deleted = 1 WHERE profile_id = ?")) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DBException("Failed to update workout profile.");
            }
        } catch (SQLException e) {
            throw new DBException("Error deleting workout: " + e.getMessage());
        }
    }
    
    @Override
    public List<WorkoutProfile> search(String query) throws DBException {
        List<WorkoutProfile> results = new ArrayList<>();
        
        if (query == null || query.isEmpty()) {
            return results;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM PUBLIC.workout_profile WHERE LOWER(profile_name) LIKE ? AND is_deleted = 0")) {
            
            stmt.setString(1, "%" + query.toLowerCase() + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("profile_id");
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    boolean isDeleted = rs.getBoolean("is_deleted");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    results.add(new WorkoutProfile(id, name, iconPath, items, isDeleted));
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error searching workout profiles: " + e.getMessage());
        }
        
        return results;
    }

    /**
     * Helper method to get workout items for a specific profile
     */
    private List<WorkoutItem> getWorkoutItemsForProfile(int profileId) throws SQLException {
        List<WorkoutItem> items = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM PUBLIC.profile_exercise WHERE profile_id = ?")) {
            
            stmt.setInt(1, profileId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int exerciseId = rs.getInt("exercise_id");
                    int sets = rs.getInt("sets");
                    int reps = rs.getInt("reps");
                    double weight = rs.getDouble("weight");
                    double duration = rs.getDouble("duration");
                    
                    try {
                        // Use the injected exerciseDB dependency
                        Exercise exercise = exerciseDB.getExerciseByID(exerciseId);
                        if (exercise != null) {
                            WorkoutItem item = duration > 0 ? 
                                new WorkoutItem(exercise, sets, reps, weight, duration) : 
                                new WorkoutItem(exercise, sets, reps, weight);
                            items.add(item);
                        }
                    } catch (DBException e) {
                        Timber.tag(TAG).e(e, "Error fetching exercise with ID %d", exerciseId);
                        // Continue with other items even if one fails
                    }
                }
            }
        }
        
        return items;
    }

    @Override
    public void close() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            Timber.tag(TAG).d("WorkoutDAO closed successfully");
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Error during WorkoutDAO close operation");
            throw e;
        }
    }
}
