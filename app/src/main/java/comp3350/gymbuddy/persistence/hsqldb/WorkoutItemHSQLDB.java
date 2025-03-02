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

    private final String dbPath;
    private final IExercisePersistence exercisePersistence;

    public WorkoutItemHSQLDB(final String dbPath, IExercisePersistence exercisePersistence) {
        this.dbPath = dbPath;
        this.exercisePersistence = exercisePersistence;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public List<WorkoutItem> getAll() {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        String query = "SELECT * FROM WorkoutItems";

        try (Connection c = connection();
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

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

}
