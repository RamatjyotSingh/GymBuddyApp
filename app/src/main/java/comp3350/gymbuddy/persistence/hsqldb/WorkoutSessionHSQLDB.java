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

    /**
     * Inserts a new workout session into the database.
     * 
     * @param session The workout session to insert.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean insertSession(WorkoutSession session) throws DBException {
        String query = "INSERT INTO workout_session (session_id, start_time, end_time, profile_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, session.getId());
            stmt.setLong(2, session.getStartTime());
            stmt.setLong(3, session.getEndTime());
            stmt.setInt(4, session.getWorkoutProfile().getId());
            
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                // Insert all workout items if we have any
                List<WorkoutItem> items = session.getWorkoutItems();
                if (items != null && !items.isEmpty()) {
                    for (WorkoutItem item : items) {
                        addExerciseToSession(session.getId(), item);
                    }
                }
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            throw new DBException("Failed to insert workout session: " + e.getMessage());
        }
    }
    
    /**
     * Adds an exercise to an existing workout session.
     * 
     * @param sessionId ID of the session.
     * @param item The workout item to add.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean addExerciseToSession(int sessionId, WorkoutItem item) throws DBException {
        String query = "INSERT INTO session_item (session_id, exercise_id, reps, weight, duration) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, sessionId);
            stmt.setInt(2, item.getExercise().getID());
            stmt.setInt(3, item.getReps());
            stmt.setDouble(4, item.getWeight());
            stmt.setDouble(5, item.getTime());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DBException("Failed to add exercise to session: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing workout session.
     * Note: Due to WorkoutSession immutability, this creates a new DB record with same ID.
     * 
     * @param session The workout session with updated values.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean updateSession(WorkoutSession session) throws DBException {
        String query = "UPDATE workout_session SET start_time = ?, end_time = ?, profile_id = ? WHERE session_id = ?";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setLong(1, session.getStartTime());
            stmt.setLong(2, session.getEndTime());
            stmt.setInt(3, session.getWorkoutProfile().getId());
            stmt.setInt(4, session.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DBException("Failed to update workout session: " + e.getMessage());
        }
    }

    /**
     * Updates just the end time of a workout session.
     * 
     * @param sessionId ID of the session to update.
     * @param endTime The new end time value.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean updateSessionEndTime(int sessionId, long endTime) throws DBException {
        String query = "UPDATE workout_session SET end_time = ? WHERE session_id = ?";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setLong(1, endTime);
            stmt.setInt(2, sessionId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DBException("Failed to update workout session end time: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a workout session and all its associated items.
     * 
     * @param sessionId ID of the session to delete.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean deleteSession(int sessionId) throws DBException {
        // First delete all related session items
        String deleteItemsQuery = "DELETE FROM session_item WHERE session_id = ?";
        String deleteSessionQuery = "DELETE FROM workout_session WHERE session_id = ?";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement deleteItemsStmt = conn.prepareStatement(deleteItemsQuery);
             PreparedStatement deleteSessionStmt = conn.prepareStatement(deleteSessionQuery)) {
             
            // Delete related items first to maintain referential integrity
            deleteItemsStmt.setInt(1, sessionId);
            deleteItemsStmt.executeUpdate();
            
            // Then delete the session
            deleteSessionStmt.setInt(1, sessionId);
            int rowsAffected = deleteSessionStmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DBException("Failed to delete workout session: " + e.getMessage());
        }
    }
    
    /**
     * Removes an exercise from a workout session.
     * 
     * @param sessionId ID of the session.
     * @param exerciseId ID of the exercise to remove.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    public boolean removeExerciseFromSession(int sessionId, int exerciseId) throws DBException {
        String query = "DELETE FROM session_item WHERE session_id = ? AND exercise_id = ?";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, sessionId);
            stmt.setInt(2, exerciseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DBException("Failed to remove exercise from session: " + e.getMessage());
        }
    }
    
    /**
     * Searches for workout sessions by matching profile name or date.
     * 
     * @param query The search query string.
     * @return A list of matching workout sessions.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    @NonNull
    public List<WorkoutSession> search(String query) throws DBException {
        List<WorkoutSession> results = new ArrayList<>();
        String searchQuery = 
            "SELECT ws.* FROM workout_session ws " +
            "JOIN workout_profile wp ON ws.profile_id = wp.profile_id " +
            "WHERE LOWER(wp.profile_name) LIKE ? OR CAST(ws.start_time AS VARCHAR(100)) LIKE ?";
        
        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
             
            String searchParam = "%" + query.toLowerCase() + "%";
            stmt.setString(1, searchParam);
            stmt.setString(2, searchParam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(extractWorkoutSession(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DBException("Failed to search workout sessions: " + e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Gets all exercises for a specific workout session.
     * 
     * @param sessionId ID of the session.
     * @return List of workout items for the session.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    @NonNull
    public List<WorkoutItem> getExercisesForSession(int sessionId) throws DBException {
        return getWorkoutItemsBySessionId(sessionId);
    }
}
