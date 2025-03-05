package comp3350.gymbuddy.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class ExerciseService {
    private final IExercisePersistence persistence;

    public ExerciseService() {
        persistence = PersistenceManager.getEngine().getExercisePersistence();
    }

    public ExerciseService(IExercisePersistence exercisePersistence) {
        persistence = exercisePersistence;
    }

    public List<Exercise> getAll() {
        return Collections.unmodifiableList(persistence.getAll());
    }

    public Exercise getExerciseByID(int id) {
        return persistence.getExerciseByID(id);
    }

    public List<Exercise> search(String searchString) {
        List<Exercise> results = new ArrayList<>();

        if (!searchString.isEmpty()) {
            for (var exercise : persistence.getAll()) {
                if (exercise.getName().toLowerCase().contains(searchString.toLowerCase())) {
                    results.add(exercise);
                }
            }
        }

        return results;
    }
}