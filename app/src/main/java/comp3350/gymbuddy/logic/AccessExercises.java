package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.IExercisePersistence;

public class AccessExercises {
    final private IExercisePersistence exercisePersistence;

    public AccessExercises() {
        exercisePersistence = Services.getExercisePersistence();
    }
    public List<Exercise> getAllExercises() {
        return Collections.unmodifiableList(exercisePersistence.getAll());
    }

    public Exercise getExerciseByID(int id){
        return exercisePersistence.getExerciseByID(id);
    }

    public List<Exercise> filterByQuery(String query){
        return exercisePersistence.filterByQuery(query);
    }
}
