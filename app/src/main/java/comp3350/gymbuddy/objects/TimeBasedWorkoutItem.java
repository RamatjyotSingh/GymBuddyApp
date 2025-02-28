package comp3350.gymbuddy.objects;

public class TimeBasedWorkoutItem extends WorkoutItem {
    private final double time;

    public TimeBasedWorkoutItem(Exercise exercise, int sets, double time) {
        super(exercise, sets);
        this.time = time;
    }

    public double getTime() {
        return time;
    }
}
