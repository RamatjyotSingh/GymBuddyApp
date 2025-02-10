package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.IExercisePersistence;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import java.util.List;

public class ExerciseService {
    private IExercisePersistence exercisePersistence;

    // Default constructor uses ExerciseStub
    public ExerciseService() {
        this.exercisePersistence = new ExerciseStub(); // Default stub implementation
    }

    public ExerciseService(IExercisePersistence exercisePersistence) {
        this.exercisePersistence = exercisePersistence;
    }

    public List<Exercise> getAllExercises() {
        return exercisePersistence.getAllExercises();
    }
}
