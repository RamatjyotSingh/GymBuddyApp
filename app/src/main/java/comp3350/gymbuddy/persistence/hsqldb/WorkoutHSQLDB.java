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
import comp3350.gymbuddy.persistence.PersistenceManager;

public class WorkoutHSQLDB implements IWorkoutDB {

    @Override
    public boolean saveWorkout(WorkoutProfile profile) throws DBException {
        if (profile == null) {
            return false;
        }

        try (Connection conn = HSQLDBHelper.getConnection()) {
            // Begin transaction
            conn.setAutoCommit(false);
            
            try {
                // First, check if profile already exists
                WorkoutProfile existingProfile = null;
                int profileId;
                
                // Check by name
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM PUBLIC.workout_profile WHERE profile_name = ?")) {
                    stmt.setString(1, profile.getName());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            profileId = rs.getInt("profile_id");
                            existingProfile = new WorkoutProfile(
                                profileId,
                                rs.getString("profile_name"),
                                rs.getString("icon_path"),
                                new ArrayList<>()
                            );
                        }
                    }
                }
                
                if (existingProfile == null) {
                    // Insert new profile
                    try (PreparedStatement stmt = conn.prepareStatement(
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
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE PUBLIC.workout_profile SET icon_path = ? WHERE profile_id = ?")) {
                        stmt.setString(1, profile.getIconPath());
                        stmt.setInt(2, profileId);
                        stmt.executeUpdate();
                    }
                    
                    // Delete existing workout items to replace with new ones
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM PUBLIC.profile_exercise WHERE profile_id = ?")) {
                        stmt.setInt(1, profileId);
                        stmt.executeUpdate();
                    }
                }
                
                // Now insert workout items
                if (profile.getWorkoutItems() != null && !profile.getWorkoutItems().isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(
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
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                throw new DBException("Error saving workout profile: " + e.getMessage());
            } finally {
                // Restore auto-commit
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            throw new DBException("Error connecting to database: " + e.getMessage());
        }
    }

    // Helper method to get next available profile ID
    private int getNextProfileId() throws SQLException {
        int nextId = 1;
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
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
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM PUBLIC.workout_profile")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("profile_id");
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    profiles.add(new WorkoutProfile(id, name, iconPath, items));
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error retrieving workout profiles: " + e.getMessage());
        }
        
        return profiles;
    }
    
    @Override
    public WorkoutProfile getWorkoutProfileById(int id) throws DBException {
        WorkoutProfile profile = null;
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM PUBLIC.workout_profile WHERE profile_id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    profile = new WorkoutProfile(id, name, iconPath, items);
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Error retrieving workout profile: " + e.getMessage());
        }
        
        return profile;
    }
    
    @Override
    public boolean deleteWorkout(int id) throws DBException {
        try (Connection conn = HSQLDBHelper.getConnection()) {
            // Begin transaction
            conn.setAutoCommit(false);
            
            try {
                // Delete profile exercises first (foreign key constraint)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM PUBLIC.profile_exercise WHERE profile_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                
                // Now delete the profile
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM PUBLIC.workout_profile WHERE profile_id = ?")) {
                    stmt.setInt(1, id);
                    int rowsAffected = stmt.executeUpdate();
                    
                    // Commit transaction
                    conn.commit();
                    return rowsAffected > 0;
                }
                
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                throw new DBException("Error deleting workout profile: " + e.getMessage());
            } finally {
                // Restore auto-commit
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            throw new DBException("Error connecting to database: " + e.getMessage());
        }
    }
    
    @Override
    public List<WorkoutProfile> search(String query) throws DBException {
        List<WorkoutProfile> results = new ArrayList<>();
        
        if (query == null || query.isEmpty()) {
            return results;
        }
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM PUBLIC.workout_profile WHERE LOWER(profile_name) LIKE ?")) {
            
            stmt.setString(1, "%" + query.toLowerCase() + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("profile_id");
                    String name = rs.getString("profile_name");
                    String iconPath = rs.getString("icon_path");
                    
                    // Get workout items for this profile
                    List<WorkoutItem> items = getWorkoutItemsForProfile(id);
                    
                    results.add(new WorkoutProfile(id, name, iconPath, items));
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
        IExerciseDB exerciseDB = PersistenceManager.getExerciseDB(true);
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM PUBLIC.profile_exercise WHERE profile_id = ?")) {
            
            stmt.setInt(1, profileId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int exerciseId = rs.getInt("exercise_id");
                    int sets = rs.getInt("sets");
                    int reps = rs.getInt("reps");
                    double weight = rs.getDouble("weight");
                    double duration = rs.getDouble("duration");
                    
                    Exercise exercise = exerciseDB.getExerciseByID(exerciseId);
                    if (exercise != null) {
                        WorkoutItem item = duration > 0 ? 
                            new WorkoutItem(exercise, sets, reps, weight, duration) : 
                            new WorkoutItem(exercise, sets, reps, weight);
                        items.add(item);
                    }
                }
            }
        }
        
        return items;
    }

    @Override
    public void close() throws Exception {
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            // CHECKPOINT ensures all data is written to disk files
            stmt.execute("CHECKPOINT");
            timber.log.Timber.d("WorkoutHSQLDB closed successfully with CHECKPOINT");
        } catch (SQLException e) {
            timber.log.Timber.e(e, "Error during WorkoutHSQLDB close operation");
            throw new DBException("Error during database cleanup: " + e.getMessage());
        }
    }
}
