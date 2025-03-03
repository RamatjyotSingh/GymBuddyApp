package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class AccessExercises extends Access {
    private final IExercisePersistence exercisePersistence;

    public AccessExercises() {
        exercisePersistence = Services.getExercisePersistence();
        this.persistence = exercisePersistence;
    }

    public AccessExercises(IExercisePersistence exercisePersistence) {
        this.exercisePersistence = exercisePersistence;
        this.persistence = exercisePersistence;
    }

    @Override
    public List<Exercise> getAll() {
        return Collections.unmodifiableList(exercisePersistence.getAll());
    }

    public Exercise getExerciseByID(int id) {
        return exercisePersistence.getExerciseByID(id);
    }

    public Exercise getExerciseByName(String name) {
        return exercisePersistence.getExerciseByName(name);
    }
}