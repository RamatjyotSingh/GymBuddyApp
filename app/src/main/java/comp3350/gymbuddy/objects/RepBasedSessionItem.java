package comp3350.gymbuddy.objects;

public class RepBasedSessionItem extends SessionItem {
    private final double weight;
    private final int reps;

    public RepBasedSessionItem(int id, WorkoutItem associatedWorkoutItem, double weight, int reps) {
        super(id, associatedWorkoutItem);
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight(){ return this.weight; }

    public int getReps() { return this.reps; }
}
