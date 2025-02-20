package comp3350.gymbuddy.objects;

public class RepBasedSessionItem extends SessionItem {
    private double weight;
    private int repetitions;

    public RepBasedSessionItem(WorkoutItem associatedWorkoutItem, double weight, int repetitions) {
        super(associatedWorkoutItem);
        this.weight = weight;
        this.repetitions = repetitions;
    }
}
