package comp3350.gymbuddy.objects;

import androidx.annotation.NonNull;

public abstract class SessionItem {
    private final WorkoutItem associatedWorkoutItem;

    public SessionItem(WorkoutItem associatedWorkoutItem) {
        this.associatedWorkoutItem = associatedWorkoutItem;
    }

    public WorkoutItem getAssociatedWorkoutItem(){ return this.associatedWorkoutItem; }

    @NonNull
    public abstract String toString();
}
