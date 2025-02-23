package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.IExercisePersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessExercises {
    final private IExercisePersistence exercisePersistence;

    public AccessExercises() {
        exercisePersistence = Services.getExercisePersistence();
    }
    public List<Exercise> getAllExercises() {
        return Collections.unmodifiableList(exercisePersistence.getAllExercises());
    }

    public Exercise getExerciseByID(int id){
        return exercisePersistence.getExerciseByID(id);
    }

    public List<Exercise> filterByQuery(String query){
        return exercisePersistence.filterByQuery(query);
    }
}
