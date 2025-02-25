package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.objects.Exercise;

import java.util.List;

public interface IExercisePersistence extends IPersistence{
    List<Exercise> getAll();
    Exercise getExerciseByName(String name);
    Exercise getExerciseByID(int id);
}
