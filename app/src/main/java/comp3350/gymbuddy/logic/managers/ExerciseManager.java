package comp3350.gymbuddy.logic.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.logic.exception.ExerciseAccessException;
import timber.log.Timber;

/**
 * Manager class for Exercise-related operations
 */
public class ExerciseManager {
    private final IExerciseDB exerciseDB;
    private static final String TAG = "ExerciseManager";

    /**
     * Creates an ExerciseManager with the specified database implementation
     * @param exerciseDB The exercise database to use
     */
    public ExerciseManager(IExerciseDB exerciseDB) {
        this.exerciseDB = exerciseDB;
    }


    /**
     * Get all exercises
     * @return List of all exercises
     * @throws ExerciseAccessException if database access fails
     */
    public List<Exercise> getAll() {
        try {
            return Collections.unmodifiableList(exerciseDB.getAll());
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve exercises");
            throw new ExerciseAccessException("Failed to retrieve exercises", e);
        }
    }

    /**
     * Get exercise by ID
     * @param id Exercise ID
     * @return Exercise object
     * @throws ExerciseAccessException if exercise not found or database access fails
     */
    public Exercise getExerciseByID(int id) {
        try {
            Exercise exercise = exerciseDB.getExerciseByID(id);
            if (exercise == null) {
                throw new ExerciseAccessException("Exercise not found with ID: " + id);
            }
            return exercise;
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to retrieve exercise with ID %d", id);
            throw new ExerciseAccessException("Failed to retrieve exercise with ID: " + id, e);
        }
    }

    /**
     * Search exercises by name
     * @param searchString Search term
     * @return List of matching exercises
     * @throws ExerciseAccessException if database access fails
     */
    public List<Exercise> search(String searchString) {
        List<Exercise> results = new ArrayList<>();

        try {
            if (searchString != null && !searchString.isEmpty()) {
                for (var exercise : exerciseDB.getAll()) {
                    if (exercise.getName().toLowerCase().contains(searchString.toLowerCase())) {
                        results.add(exercise);
                    }
                }
            }
            return results;
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Failed to search exercises");
            throw new ExerciseAccessException("Failed to search exercises", e);
        }
    }


}