package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.ExercisePersistence;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class ExerciseStub implements ExercisePersistence{
    private List<Exercise> exercises;

    public ExerciseStub(){
        this.exercises = new ArrayList<Exercise>();

        // put placeholder data into list here
        // need to fill out exercise object implementation first
        // decide what goes into the object
    }

    @Override
    public List<Exercise> getExercises(){
        return Collections.unmodifiableList(exercises);
    }
}
