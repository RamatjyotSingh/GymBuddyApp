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
        String query = "SELECT id, name, iconPath FROM WORKOUTPROFILE"; // Assuming the columns in your table

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workoutProfiles;
    }
}
