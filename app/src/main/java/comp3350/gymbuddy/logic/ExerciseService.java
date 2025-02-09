package comp3350.gymbuddy.business;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.IExercisePersistence;

import java.util.List;

public class ExerciseService {
    private IExercisePersistence exercisePersistence;

    public ExerciseService(IExercisePersistence exercisePersistence) {
        this.exercisePersistence = exercisePersistence;
    }

    public List<Exercise> getAllExercises() {
        return exercisePersistence.getAllExercises();
    }
}
