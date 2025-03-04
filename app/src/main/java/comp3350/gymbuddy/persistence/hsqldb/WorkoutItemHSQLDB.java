package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class WorkoutItemHSQLDB implements IWorkoutItemPersistence {

    private final Connection connection;
    private final IExercisePersistence exercisePersistence;

    public WorkoutItemHSQLDB(final Connection connection, IExercisePersistence exercisePersistence) {
        this.connection = connection;
        this.exercisePersistence = exercisePersistence;
    }

    @Override
    public void insertWorkoutItem(WorkoutItem item, int profileId) {
        try {
            String query = "INSERT INTO WORKOUT_ITEMS (profileId, exerciseId, sets, reps, weight, time) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = connection.prepareStatement(query)) {
                st.setInt(1, profileId);
                st.setInt(2, item.getExercise().getID()); // Links to EXERCISE
                st.setInt(3, item.getSets());
                st.setInt(3, item.getReps());
                st.setDouble(4, item.getWeight());
                st.setDouble(4, item.getTime());
                st.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting workout item", e);
        }
    }

    @Override
    public List<WorkoutItem> getWorkoutItemsByProfileId(int profileId) {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        // Get all workout items
        String query = "SELECT id, exerciseId, sets, reps, weight, time FROM WORKOUT_ITEMS WHERE profileId = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, profileId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    workoutItems.add(constructWorkoutItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving workout items", e);
        }

        return workoutItems;
    }

    @Override
    public List<WorkoutItem> getAll() {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        // Get all workout items
        String query = "SELECT id, exerciseId, sets, reps, weight, time FROM WORKOUT_ITEMS";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                workoutItems.add(constructWorkoutItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving workout items", e);
        }

        return workoutItems;
    }

    @Override
    public WorkoutItem getWorkoutItemById(int workoutItemId) {
        // Check workout items
        String query = "SELECT id, exerciseId, sets, reps, weight FROM WORKOUT_ITEMS WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, workoutItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructWorkoutItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving workout item", e);
        }

        return null; // Return null if no workout item is found
    }

    private WorkoutItem constructWorkoutItem(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exerciseId");
        int sets = rs.getInt("sets");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");
        double time = rs.getDouble("time");

        Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
        if (exercise == null) return null;

        return new WorkoutItem(exercise, sets, reps, weight, time);
    }
}
