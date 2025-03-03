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
        String query = "INSERT INTO SESSIONITEM (workoutSessionId, workoutItemId, type, reps, weight, time) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, workoutSessionId); // Foreign key linking to WORKOUTSESSION
            st.setInt(2, sessionItem.getAssociatedWorkoutItem().getExercise().getID()); // Foreign key linking to WORKOUTITEMS

            if (sessionItem instanceof RepBasedSessionItem) {
                RepBasedSessionItem repItem = (RepBasedSessionItem) sessionItem;
                st.setString(3, "Rep");
                st.setInt(4, repItem.getReps());
                st.setDouble(5, repItem.getWeight());
                st.setNull(6, java.sql.Types.INTEGER);
            } else if (sessionItem instanceof TimeBasedSessionItem) {
                TimeBasedSessionItem timeItem = (TimeBasedSessionItem) sessionItem;
                st.setString(3, "Time");
                st.setNull(4, java.sql.Types.INTEGER);
                st.setNull(5, java.sql.Types.DOUBLE);
                st.setDouble(6, timeItem.getTime());
            }

            st.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<SessionItem> getAll() {
        List<SessionItem> sessionItems = new ArrayList<>();
        String query = "SELECT id, workoutItemId, type, reps, weight, time FROM SESSIONITEM";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int workoutItemId = rs.getInt("workoutItemId");
                String type = rs.getString("type");
                Integer reps = (rs.getObject("reps") != null) ? rs.getInt("reps") : null;
                Double weight = (rs.getObject("weight") != null) ? rs.getDouble("weight") : null;
                Double time = (rs.getObject("time") != null) ? rs.getDouble("time") : null;

                // Fetch the corresponding WorkoutItem
                WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
                if (workoutItem == null) continue;

                // Create the appropriate SessionItem object
                SessionItem sessionItem;
                if ("Rep".equals(type)) {
                    sessionItem = new RepBasedSessionItem(workoutItem, weight != null ? weight : 0, reps != null ? reps : 0);
                } else {
                    sessionItem = new TimeBasedSessionItem(workoutItem, time != null ? time : 0);
                }

                sessionItems.add(sessionItem);
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

        return sessionItems;
    }

    @Override
    public SessionItem getSessionItemById(int sessionItemId) {
        String query = "SELECT id, workoutItemId, type, reps, weight, time FROM SESSIONITEM WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, sessionItemId);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int workoutItemId = rs.getInt("workoutItemId");
                    String type = rs.getString("type");
                    Integer reps = (rs.getObject("reps") != null) ? rs.getInt("reps") : null;
                    Double weight = (rs.getObject("weight") != null) ? rs.getDouble("weight") : null;
                    Double time = (rs.getObject("time") != null) ? rs.getDouble("time") : null;

                    // Fetch the corresponding WorkoutItem
                    WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
                    if (workoutItem == null) return null;

                    // Create the appropriate SessionItem object
                    if ("Rep".equals(type)) {
                        return new RepBasedSessionItem(workoutItem, weight != null ? weight : 0, reps != null ? reps : 0);
                    } else {
                        return new TimeBasedSessionItem(workoutItem, time != null ? time : 0);
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving session item by ID", e);
        }

        return null; // Return null if no session item is found
    }

    @Override
    public List<SessionItem> getSessionItemsBySessionId(int sessionId) {
        List<SessionItem> sessionItems = new ArrayList<>();
        String query = "SELECT id, workoutItemId, type, reps, weight, time FROM SESSIONITEM WHERE workoutSessionId = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, sessionId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    sessionItems.add(constructSessionItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving session items for session ID: " + sessionId, e);
        }

        return sessionItems;
    }

    private SessionItem constructSessionItem(ResultSet rs) throws SQLException {
        int workoutItemId = rs.getInt("workoutItemId");
        String type = rs.getString("type");
        Integer reps = (rs.getObject("reps") != null) ? rs.getInt("reps") : null;
        Double weight = (rs.getObject("weight") != null) ? rs.getDouble("weight") : null;
        Double time = (rs.getObject("time") != null) ? rs.getDouble("time") : null;

        // Retrieve the corresponding WorkoutItem
        WorkoutItem workoutItem = workoutItemPersistence.getWorkoutItemById(workoutItemId);
        if (workoutItem == null) return null;

        // Create the appropriate SessionItem object
        if ("Rep".equals(type)) {
            return new RepBasedSessionItem(workoutItem, weight != null ? weight : 0, reps != null ? reps : 0);
        } else {
            return new TimeBasedSessionItem(workoutItem, time != null ? time : 0);
        }
    }
}
