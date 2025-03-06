package comp3350.gymbuddy.logic.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseManager {
    private final IExerciseDB exerciseDB;

    public ExerciseManager(boolean forProduction) {
        exerciseDB = PersistenceManager.getExerciseDB(forProduction);
    }

    public List<Exercise> getAll() {
        return Collections.unmodifiableList(exerciseDB.getAll());
    }

    public Exercise getExerciseByID(int id) {
        return exerciseDB.getExerciseByID(id);
    }

    public List<Exercise> search(String searchString) {
        List<Exercise> results = new ArrayList<>();

        if (!searchString.isEmpty()) {
            for (var exercise : exerciseDB.getAll()) {
                if (exercise.getName().toLowerCase().contains(searchString.toLowerCase())) {
                    results.add(exercise);
                }
            }
        }

        return results;
    }
}