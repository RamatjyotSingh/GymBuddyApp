package comp3350.gymbuddy.objects;

public abstract class SessionItem {
    private WorkoutItem associatedWorkoutItem;

    public SessionItem(WorkoutItem associatedWorkoutItem) {
        this.associatedWorkoutItem = associatedWorkoutItem;
    }
}
