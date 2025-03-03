package comp3350.gymbuddy.persistence.hsqldb;

import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionHSQLDB implements IWorkoutSessionPersistence {

    private final Connection connection;
    private final ISessionItemPersistence sessionItemPersistence;
    private final IWorkoutProfilePersistence workoutProfilePersistence;

    public WorkoutSessionHSQLDB(final Connection connection, ISessionItemPersistence sessionItemPersistence, IWorkoutProfilePersistence workoutProfilePersistence) {
        this.connection = connection;
        this.sessionItemPersistence = sessionItemPersistence; // Use SessionItemHSQLDB
        this.workoutProfilePersistence = workoutProfilePersistence;
    }

    @Override
    public void insertWorkoutSession(WorkoutSession session) {
        String query = "INSERT INTO WORKOUTSESSION (startTime, endTime, profileId) VALUES (?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, session.getStartTime());  // Start time in milliseconds
            st.setLong(2, session.getEndTime());  // End time in milliseconds
            st.setInt(3, session.getWorkoutProfile().getId()); // Foreign key: profileId

            st.executeUpdate();

            // Retrieve generated WorkoutSession ID
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int sessionId = generatedKeys.getInt(1); // Get auto-generated session ID
                    insertSessionItems(sessionId, session); // Insert session items
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting workout session", e);
        }
    }

    private void insertSessionItems(int sessionId, WorkoutSession session) {
        session.getSessionItems().forEach(sessionItem -> sessionItemPersistence.insertSessionItem(sessionId, sessionItem));
    }

    @Override
    public List<WorkoutSession> getAll() {
        List<WorkoutSession> workoutSessions = new ArrayList<>();
        String query = "SELECT id, startTime, endTime, profileId FROM WORKOUTSESSION";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                long startTime = rs.getLong("startTime");
                long endTime = rs.getLong("endTime");
                int profileId = rs.getInt("profileId");

                // Fetch associated WorkoutProfile
                WorkoutProfile profile = workoutProfilePersistence.getWorkoutProfileById(profileId);
                if (profile == null) continue; // Skip if profile doesn't exist

                // Fetch all session items related to this session
                List<SessionItem> sessionItems = sessionItemPersistence.getSessionItemsBySessionId(id);

                // Create the WorkoutSession object
                WorkoutSession session = new WorkoutSession(startTime, endTime, sessionItems, profile);
                workoutSessions.add(session);
            }
        } catch (final SQLException e) {
            throw new PersistenceException("Error retrieving workout sessions", e);
        }

        return workoutSessions;
    }
}
