package comp3350.gymbuddy.persistence.hsqldb;

import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
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
        try {
            if (sessionItem instanceof RepBasedSessionItem) {
                String query = "INSERT INTO SESSION_ITEM_REP (workoutSessionId, workoutItemId, reps, weight) VALUES (?, ?, ?, ?)";
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.setInt(1, workoutSessionId);
                    st.setInt(2, sessionItem.getAssociatedWorkoutItem().getExercise().getID()); // References WORKOUTITEMS_REP
                    st.setInt(3, ((RepBasedSessionItem) sessionItem).getReps());
                    st.setDouble(4, ((RepBasedSessionItem) sessionItem).getWeight());
                    st.executeUpdate();
                }
            } else if (sessionItem instanceof TimeBasedSessionItem) {
                String query = "INSERT INTO SESSION_ITEM_TIME (workoutSessionId, workoutItemId, time) VALUES (?, ?, ?)";
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.setInt(1, workoutSessionId);
                    st.setInt(2, sessionItem.getAssociatedWorkoutItem().getExercise().getID()); // References WORKOUTITEMS_TIME
                    st.setDouble(3, ((TimeBasedSessionItem) sessionItem).getTime());
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting session item", e);
        }
    }

    @Override
    public List<SessionItem> getAll() {
        List<SessionItem> sessionItems = new ArrayList<>();

        // Get all rep-based session items
        String repQuery = "SELECT id, workoutItemId, reps, weight FROM SESSION_ITEM_REP";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(repQuery)) {
            while (rs.next()) {
                sessionItems.add(constructRepBasedSessionItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session items", e);
        }

        // Get all time-based session items
        String timeQuery = "SELECT id, workoutItemId, time FROM SESSION_ITEM_TIME";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(timeQuery)) {
            while (rs.next()) {
                sessionItems.add(constructTimeBasedSessionItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based session items", e);
        }

        return sessionItems;
    }

    @Override
    public SessionItem getSessionItemById(int sessionItemId) {
        // Check rep-based session items
        String repQuery = "SELECT id, workoutItemId, reps, weight FROM SESSION_ITEM_REP WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, sessionItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructRepBasedSessionItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session item", e);
        }

        // Check time-based session items
        String timeQuery = "SELECT id, workoutItemId, time FROM SESSION_ITEM_TIME WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(timeQuery)) {
            st.setInt(1, sessionItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructTimeBasedSessionItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based session item", e);
        }

        return null; // Return null if no session item is found
    }

    @Override
    public List<SessionItem> getSessionItemsBySessionId(int sessionId) {
        List<SessionItem> sessionItems = new ArrayList<>();

        // Get rep-based session items
        String repQuery = "SELECT id, workoutItemId, reps, weight FROM SESSION_ITEM_REP WHERE workoutSessionId = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, sessionId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    sessionItems.add(constructRepBasedSessionItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based session items for session ID: " + sessionId, e);
        }

        // Get time-based session items
        String timeQuery = "SELECT id, workoutItemId, time FROM SESSION_ITEM_TIME WHERE workoutSessionId = ?";
        try (PreparedStatement st = connection.prepareStatement(timeQuery)) {
            st.setInt(1, sessionId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    sessionItems.add(constructTimeBasedSessionItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based session items for session ID: " + sessionId, e);
        }

        return sessionItems;
    }

    private SessionItem constructRepBasedSessionItem(ResultSet rs) throws SQLException {
        int workoutItemId = rs.getInt("workoutItemId");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");

        // Fetch associated WorkoutItem from WORKOUTITEMS_REP
        WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
        if (workoutItem == null) return null;

        return new RepBasedSessionItem(workoutItem, weight, reps);
    }

    private SessionItem constructTimeBasedSessionItem(ResultSet rs) throws SQLException {
        int workoutItemId = rs.getInt("workoutItemId");
        double time = rs.getDouble("time");

        // Fetch associated WorkoutItem from WORKOUTITEMS_TIME
        WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
        if (workoutItem == null) return null;

        return new TimeBasedSessionItem(workoutItem, time);
    }
}
