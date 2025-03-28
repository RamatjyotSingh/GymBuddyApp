package comp3350.gymbuddy.logic.managers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.logic.exception.WorkoutAccessException;
import timber.log.Timber;

/**
 * Manager class for Workout-related operations
 */
public class WorkoutManager {
    private final IWorkoutDB workoutDB;
    private static final String TAG = "WorkoutManager";

    /**
     * Creates a WorkoutManager with the specified database implementation
     * @param workoutDB The workout database to use
     */
    public WorkoutManager(IWorkoutDB workoutDB) {
        this.workoutDB = workoutDB;
    }



    /**
     * Get all saved (non-deleted) workouts
     * @return List of workout profiles
     * @throws WorkoutAccessException if database access fails
     */
    public List<WorkoutProfile> getSavedWorkouts() {
        try {
            // Collect only the workouts that have not been deleted.
            return Collections.unmodifiableList(workoutDB.getAll());
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve saved workouts");
            throw new WorkoutAccessException("Failed to retrieve saved workouts", e);
        }
    }

    /**
     * Save a workout profile
     * @param workoutProfile Workout to save
     * @throws WorkoutAccessException if saving fails
     */
    public void saveWorkout(WorkoutProfile workoutProfile) {
        try {
            workoutDB.saveWorkout(workoutProfile);
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to save workout");
            throw new WorkoutAccessException("Failed to save workout", e);
        }
    }

    /**
     * Get a workout profile by ID
     * @param id Workout ID
     * @return Workout profile
     * @throws WorkoutAccessException if workout not found or database access fails
     */
    public WorkoutProfile getWorkoutProfileByID(int id) {
        try {
            WorkoutProfile workout = workoutDB.getWorkoutProfileById(id);
            if (workout == null) {
                throw new WorkoutAccessException("Workout not found with ID: " + id);
            }
            return workout;
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve workout with ID %d", id);
            throw new WorkoutAccessException("Failed to retrieve workout with ID: " + id, e);
        }
    }

    /**
     * Delete a workout by ID
     * @param id Workout ID to delete
     * @throws WorkoutAccessException if deletion fails
     */
    public void deleteWorkout(int id) {
        try {
            workoutDB.deleteWorkout(id);
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to delete workout with ID %d", id);
            throw new WorkoutAccessException("Failed to delete workout with ID: " + id, e);
        }
    }
}
