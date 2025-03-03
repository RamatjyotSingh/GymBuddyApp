package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;

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

    public void insertWorkoutItem(WorkoutItem item, int profileId){
        if (item != null && this.persistence instanceof IWorkoutItemPersistence) {
            ((IWorkoutItemPersistence) this.persistence).insertWorkoutItem(item,profileId);
        }
    }

    public List<WorkoutItem> getWorkoutItemsByProfileId(int profileId){
        return (this.persistence instanceof IWorkoutItemPersistence)
                ? ((IWorkoutItemPersistence) this.persistence).getWorkoutItemsByProfileId(profileId)
                : Collections.emptyList();
    }

    public WorkoutItem getWorkoutItemById(int workoutItemId){
        return (this.persistence instanceof IWorkoutItemPersistence)
                ? ((IWorkoutItemPersistence) this.persistence).getWorkoutItemById(workoutItemId)
                : null;
    }
}
