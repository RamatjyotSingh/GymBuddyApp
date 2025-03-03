package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
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
            if (item instanceof RepBasedWorkoutItem) {
                String query = "INSERT INTO WORKOUTITEMS_REP (profileId, exerciseId, sets, reps, weight) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.setInt(1, profileId);
                    st.setInt(2, item.getExercise().getID()); // Links to EXERCISE
                    st.setInt(3, item.getSets());
                    st.setInt(4, ((RepBasedWorkoutItem) item).getReps());
                    st.setDouble(5, ((RepBasedWorkoutItem) item).getWeight());
                    st.executeUpdate();
                }
            } else if (item instanceof TimeBasedWorkoutItem) {
                String query = "INSERT INTO WORKOUTITEMS_TIME (profileId, exerciseId, sets, time) VALUES (?, ?, ?, ?)";
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.setInt(1, profileId);
                    st.setInt(2, item.getExercise().getID()); // Links to EXERCISE
                    st.setInt(3, item.getSets());
                    st.setDouble(4, ((TimeBasedWorkoutItem) item).getTime());
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting workout item", e);
        }
    }

    @Override
    public List<WorkoutItem> getWorkoutItemsByProfileId(int profileId) {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        // Get all rep-based workout items
        String repQuery = "SELECT id, exerciseId, sets, reps, weight FROM WORKOUTITEMS_REP WHERE profileId = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, profileId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    workoutItems.add(constructRepBasedWorkoutItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based workout items", e);
        }

        // Get all time-based workout items
        String timeQuery = "SELECT id, exerciseId, sets, time FROM WORKOUTITEMS_TIME WHERE profileId = ?";
        try (PreparedStatement st = connection.prepareStatement(timeQuery)) {
            st.setInt(1, profileId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    workoutItems.add(constructTimeBasedWorkoutItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based workout items", e);
        }

        return workoutItems;
    }

    @Override
    public List<WorkoutItem> getAll() {
        List<WorkoutItem> workoutItems = new ArrayList<>();

        // Get all rep-based workout items
        String repQuery = "SELECT id, exerciseId, sets, reps, weight FROM WORKOUTITEMS_REP";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(repQuery)) {
            while (rs.next()) {
                workoutItems.add(constructRepBasedWorkoutItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based workout items", e);
        }

        // Get all time-based workout items
        String timeQuery = "SELECT id, exerciseId, sets, time FROM WORKOUTITEMS_TIME";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(timeQuery)) {
            while (rs.next()) {
                workoutItems.add(constructTimeBasedWorkoutItem(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based workout items", e);
        }

        return workoutItems;
    }

    @Override
    public WorkoutItem getWorkoutItemById(int workoutItemId) {
        // Check rep-based workout items
        String repQuery = "SELECT id, exerciseId, sets, reps, weight FROM WORKOUTITEMS_REP WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(repQuery)) {
            st.setInt(1, workoutItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructRepBasedWorkoutItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving rep-based workout item", e);
        }

        // Check time-based workout items
        String timeQuery = "SELECT id, exerciseId, sets, time FROM WORKOUTITEMS_TIME WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(timeQuery)) {
            st.setInt(1, workoutItemId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return constructTimeBasedWorkoutItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error retrieving time-based workout item", e);
        }

        return null; // Return null if no workout item is found
    }

    private WorkoutItem constructRepBasedWorkoutItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int exerciseId = rs.getInt("exerciseId");
        int sets = rs.getInt("sets");
        int reps = rs.getInt("reps");
        double weight = rs.getDouble("weight");

        Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
        if (exercise == null) return null;

        return new RepBasedWorkoutItem(exercise, sets, reps, weight);
    }

    private WorkoutItem constructTimeBasedWorkoutItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int exerciseId = rs.getInt("exerciseId");
        int sets = rs.getInt("sets");
        double time = rs.getDouble("time");

        Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
        if (exercise == null) return null;

        return new TimeBasedWorkoutItem(exercise, sets, time);
    }
}
