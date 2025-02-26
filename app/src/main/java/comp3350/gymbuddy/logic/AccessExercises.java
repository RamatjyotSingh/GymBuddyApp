package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.IExercisePersistence;

public class AccessExercises extends Access{
    public AccessExercises() {
        persistence = Services.getExercisePersistence();
    }

    public AccessExercises(final IExercisePersistence exercisePersistence){
        this();
        this.persistence = exercisePersistence;
    }

    @Override
    public List<Exercise> getAll(){
        return Collections.unmodifiableList(persistence.getAll());
    }

    public Exercise getExerciseByID(int id){
        Exercise result = null;

        if(persistence instanceof IExercisePersistence){
            result = ((IExercisePersistence)persistence).getExerciseByID(id);
        }

        return result;
    }
}
