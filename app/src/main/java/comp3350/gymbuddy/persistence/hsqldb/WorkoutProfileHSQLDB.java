package comp3350.gymbuddy.persistence.hsqldb;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutProfileHSQLDB implements IWorkoutProfilePersistence {

    private final Connection connection;

    public WorkoutProfileHSQLDB(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<WorkoutProfile> getAll() {
        List<WorkoutProfile> workoutProfiles = new ArrayList<>();
        String query = "SELECT id, name, iconPath FROM WORKOUTPROFILE";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id"); //no id variable in workoutProfile
                String name = rs.getString("name");
                String iconPath = rs.getString("iconPath");

                // Create WorkoutProfile object
                WorkoutProfile profile = new WorkoutProfile(name, iconPath, null); // Assuming no WorkoutItems are loaded at this point
                //profile.setId(id);  // Assuming you've created a setter for id in WorkoutProfile

                // Add to the list
                workoutProfiles.add(profile);
            }

        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

        return workoutProfiles;
    }

    @Override
    public void insertWorkoutProfile(WorkoutProfile profile) {
        String query = "INSERT INTO WORKOUTPROFILE (name, iconPath) VALUES (?, ?)";

        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, profile.getName());
            st.setString(2, profile.getIconPath());
            st.executeUpdate();

            // Retrieve the generated ID and assign it to the WorkoutProfile object
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profile.setId(generatedKeys.getInt(1)); // Assign the auto-generated ID
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public WorkoutProfile getWorkoutProfileById(int profileId) {
        String query = "SELECT id, name, iconPath FROM WORKOUTPROFILE WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, profileId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String iconPath = rs.getString("iconPath");

                    // Create WorkoutProfile object
                    return new WorkoutProfile(id, name, iconPath, new ArrayList<>()); // Empty workoutItems for now
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

        return null; // No profile found
    }



}
