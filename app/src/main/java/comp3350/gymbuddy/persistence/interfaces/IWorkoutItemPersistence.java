package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;

public interface IWorkoutItemPersistence extends IPersistence{
    List<WorkoutItem> getAll();
    void insertWorkoutItem(WorkoutItem item, int profileId); // Insert a new workout item

    List<WorkoutItem> getWorkoutItemsByProfileId(int profileId);

    WorkoutItem getWorkoutItemById(int workoutItemId);
}
