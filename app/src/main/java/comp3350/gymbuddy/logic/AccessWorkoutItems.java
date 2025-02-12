package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;
import comp3350.gymbuddy.persistence.stubs.WorkoutItemStub;

public class AccessWorkoutItems {
    // these persistence classes are singletons - only 1 instance of them for the lifetime of the application
    // meaning there will only be 1 database connection the entire time
    private IWorkoutItemPersistence workoutItemPersistence;

    // Default constructor uses ExerciseStub
    public AccessWorkoutItems() {
        if(this.workoutItemPersistence != null){
            this.workoutItemPersistence = new WorkoutItemStub(); // Default stub implementation
        }
    }

    public AccessWorkoutItems(IWorkoutItemPersistence exercisePersistence) {
        if(this.workoutItemPersistence != null){
            this.workoutItemPersistence = exercisePersistence;
        }
    }

    public List<WorkoutItem> getAllWorkoutItems() {
        return this.workoutItemPersistence.getAllWorkoutItems();
    }
}
