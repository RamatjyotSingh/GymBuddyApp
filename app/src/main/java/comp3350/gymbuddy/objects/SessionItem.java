package comp3350.gymbuddy.objects;

public abstract class SessionItem {
    private int id;
    private final WorkoutItem associatedWorkoutItem;

    public SessionItem(int id, WorkoutItem associatedWorkoutItem) {
        this.id = id;
        this.associatedWorkoutItem = associatedWorkoutItem;
    }

    public WorkoutItem getAssociatedWorkoutItem(){ return this.associatedWorkoutItem; }

    public int getID(){ return this.id; }
}
