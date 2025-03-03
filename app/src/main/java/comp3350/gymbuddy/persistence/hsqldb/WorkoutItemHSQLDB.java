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
        String query = "INSERT INTO WORKOUTITEMS (profileId, exerciseId, sets, type, reps, weight, time) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, profileId);  // Foreign key: links to WORKOUTPROFILE
            st.setInt(2, item.getExercise().getID()); // Foreign key: links to EXERCISE
            st.setInt(3, item.getSets()); // Number of sets
            st.setString(4, (item instanceof RepBasedWorkoutItem) ? "Rep" : "Time"); // Determine type

            // Handle NULL values correctly
            if (item instanceof RepBasedWorkoutItem) {
                RepBasedWorkoutItem repItem = (RepBasedWorkoutItem) item;
                st.setInt(5, repItem.getReps());
                st.setDouble(6, repItem.getWeight());
                st.setNull(7, java.sql.Types.INTEGER);
            } else if (item instanceof TimeBasedWorkoutItem) {
                TimeBasedWorkoutItem timeItem = (TimeBasedWorkoutItem) item;
                st.setNull(5, java.sql.Types.INTEGER);
                st.setNull(6, java.sql.Types.DOUBLE);
                st.setInt(7, (int) timeItem.getTime()); // Convert to int if needed
            }

            // Execute the insert
            st.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


    @Override
    public List<WorkoutItem> getWorkoutItemsByProfileId(int profileId) {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        String query = "SELECT * FROM WORKOUTITEMS WHERE profileId = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, profileId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int exerciseId = rs.getInt("exerciseId");
                    int sets = rs.getInt("sets");
                    String type = rs.getString("type");

                    // Get the exercise object
                    Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
                    if (exercise == null) continue; // Skip if exercise not found

                    // Handle Rep-Based or Time-Based workout items
                    if (type.equals("Rep")) {
                        int reps = rs.getInt("reps");
                        double weight = rs.getDouble("weight");
                        workoutItems.add(new RepBasedWorkoutItem(exercise, sets, reps, weight));
                    } else if (type.equals("Time")) {
                        double time = rs.getDouble("time");
                        workoutItems.add(new TimeBasedWorkoutItem(exercise, sets, time));
                    }
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
        return workoutItems;
    }



    @Override
    public List<WorkoutItem> getAll() {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        String query = "SELECT * FROM WorkoutItems";

        try( Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int exerciseId = rs.getInt("exerciseId");
                int sets = rs.getInt("sets");
                String type = rs.getString("type");

                // Get exercise object
                Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
                if (exercise == null) continue; // Skip if exercise not found

                // Handle different workout types
                if (type.equals("Rep")) {
                    int reps = rs.getInt("reps");
                    double weight = rs.getDouble("weight");
                    workoutItems.add(new RepBasedWorkoutItem(exercise, sets, reps, weight));
                } else if (type.equals("Time")) {
                    double time = rs.getDouble("time");
                    workoutItems.add(new TimeBasedWorkoutItem(exercise, sets, time));
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
        return workoutItems;
    }

    @Override
    public WorkoutItem getWorkoutItemById(int workoutItemId) {
        String query = "SELECT id, exerciseId, sets, type, reps, weight, time FROM WORKOUTITEMS WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, workoutItemId);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int exerciseId = rs.getInt("exerciseId");
                    int sets = rs.getInt("sets");
                    String type = rs.getString("type");
                    Integer reps = (rs.getObject("reps") != null) ? rs.getInt("reps") : null;
                    Double weight = (rs.getObject("weight") != null) ? rs.getDouble("weight") : null;
                    Double time = (rs.getObject("time") != null) ? rs.getDouble("time") : null;

                    // Fetch the corresponding Exercise
                    Exercise exercise = exercisePersistence.getExerciseByID(exerciseId);
                    if (exercise == null) return null;

                    // Create and return the appropriate WorkoutItem object
                    if ("Rep".equals(type)) {
                        return new RepBasedWorkoutItem(exercise, sets, reps != null ? reps : 0, weight != null ? weight : 0.0);
                    } else if ("Time".equals(type)) {
                        return new TimeBasedWorkoutItem(exercise, sets, time != null ? time : 0);
                    }
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException("Error retrieving workout item by ID", e);
        }

        return null; // Return null if no workout item is found
    }


}
