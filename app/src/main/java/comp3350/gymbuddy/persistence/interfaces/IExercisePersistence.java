package comp3350.gymbuddy.persistence.interfaces;

import comp3350.gymbuddy.objects.Exercise;

import java.util.List;

public interface IExercisePersistence {
    List<Exercise> getAll();
    Exercise getExerciseByName(String name);
    Exercise getExerciseByID(int id);
}
