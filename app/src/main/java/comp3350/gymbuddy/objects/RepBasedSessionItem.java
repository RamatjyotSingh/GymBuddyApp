package comp3350.gymbuddy.objects;

public class RepBasedSessionItem extends SessionItem {
    private double weight;
    private int reps;

    public RepBasedSessionItem(WorkoutItem associatedWorkoutItem, double weight, int reps) {
        super(associatedWorkoutItem);
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight(){ return this.weight; }

    public int getReps() { return this.reps; }
}
