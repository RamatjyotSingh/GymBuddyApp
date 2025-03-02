package comp3350.gymbuddy.objects;

public class TimeBasedSessionItem extends SessionItem {
    private final double time;

    public TimeBasedSessionItem(int id, WorkoutItem associatedWorkoutItem, double time) {
        super(id, associatedWorkoutItem);
        this.time = time;
    }

    public double getTime(){ return this.time; }
}
