package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class AccessExercises {
    private IExercisePersistence exercisePersistence;

    public AccessExercises() {
        exercisePersistence = Services.getExercisePersistence();
    }

    public AccessExercises(final IExercisePersistence exercisePersistence){
        this();
        this.exercisePersistence = exercisePersistence;
    }

    public List<Exercise> getAllExercises() {
        return Collections.unmodifiableList(exercisePersistence.getAll());
    }

    public Exercise getExerciseByID(int id){
        return exercisePersistence.getExerciseByID(id);
    }
}
