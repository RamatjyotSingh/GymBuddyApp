package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class AccessWorkoutItems {
    private IWorkoutItemPersistence workoutItemPersistence;

    public AccessWorkoutItems() {
        workoutItemPersistence = Services.getWorkoutItemPersistence();
    }

    public List<WorkoutItem> getAllWorkoutItems() {
        return Collections.unmodifiableList(workoutItemPersistence.getAllWorkoutItems());
    }
}
