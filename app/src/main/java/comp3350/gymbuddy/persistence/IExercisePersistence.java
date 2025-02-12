package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.objects.Exercise;

import java.util.List;

public interface IExercisePersistence {
    List<Exercise> getAllExercises();
}
