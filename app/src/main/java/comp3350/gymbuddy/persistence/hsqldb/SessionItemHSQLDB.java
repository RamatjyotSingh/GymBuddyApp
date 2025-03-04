package comp3350.gymbuddy.persistence.hsqldb;

import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionItemHSQLDB implements ISessionItemPersistence {

    private final Connection connection;
    private final IWorkoutItemPersistence workoutItemPersistence;

    public SessionItemHSQLDB(final Connection connection, IWorkoutItemPersistence workoutItemPersistence) {
        this.connection = connection;
        this.workoutItemPersistence = workoutItemPersistence;
    }

    @Override
    public void insertSessionItem(int workoutSessionId, SessionItem sessionItem) {
        String query = "INSERT INTO SESSION_ITEMS (workoutSessionId, workoutItemId, reps, weight, time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, workoutSessionId);
            st.setInt(2, sessionItem.getAssociatedWorkoutItem().getExercise().getID()); // References WORKOUT_ITEMS
            st.setInt(3, sessionItem.getReps());
            st.setDouble(4, sessionItem.getWeight());
            st.setDouble(5, sessionItem.getTime());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting session item", e);
        }
    }

    @Override
    public List<SessionItem> getAll() {
        List<SessionItem> sessionItems = new ArrayList<>();

        // Get all session items
        String query = "SELECT id, workoutItemId, reps, weight, time FROM SESSION_ITEMS";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                sessionItems.add(constructSessionItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session items", e);
        }

        return sessionItems;
    }

    @Override
    public SessionItem getSessionItemById(int sessionItemId) {
        // Check rep-based session items
        String repQuery = "SELECT id, workoutItemId, reps, weight, time FROM SESSION_ITEMS WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, sessionItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructSessionItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session item", e);
        }

        return null; // Return null if no session item is found
    }

    @Override
    public List<SessionItem> getSessionItemsBySessionId(int sessionId) {
        List<SessionItem> sessionItems = new ArrayList<>();

        // Get rep-based session items
        String repQuery = "SELECT id, workoutItemId, reps, weight, time FROM SESSION_ITEMS WHERE workoutSessionId = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, sessionId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    sessionItems.add(constructSessionItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session items for session ID: " + sessionId, e);
        }

        return sessionItems;
    }

    private SessionItem constructSessionItem(ResultSet rs) throws SQLException {
        int workoutItemId = rs.getInt("workoutItemId");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("time");

        // Fetch associated WorkoutItem from WORKOUT_ITEMS
        WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
        if (workoutItem == null) return null;

        return new SessionItem(workoutItem, reps, weight, time);
    }
}
