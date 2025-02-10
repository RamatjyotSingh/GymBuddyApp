package comp3350.gymbuddy.persistence;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;

public interface IWorkoutItemPersistence {
    public List<WorkoutItem> getAllWorkoutItems();
}
