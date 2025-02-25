package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class AccessWorkoutItems extends Access{
    public AccessWorkoutItems(){
        this.persistence = Services.getWorkoutItemPersistence();
    }

    public List<WorkoutItem> getAll(){
        return Collections.unmodifiableList(this.persistence.getAll());
    }

    public AccessWorkoutItems(final IWorkoutItemPersistence workoutItemPersistence){
        this();
        this.persistence = workoutItemPersistence;
    }
}
