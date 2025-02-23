package comp3350.gymbuddy.objects;

public class TimeBasedSessionItem extends SessionItem {
    private double time;

    public TimeBasedSessionItem(WorkoutItem associatedWorkoutItem, double time) {
        super(associatedWorkoutItem);
        this.time = time;
    }

    public double getTime(){ return this.time; }
}
