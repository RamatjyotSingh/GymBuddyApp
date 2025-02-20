package comp3350.gymbuddy.objects;

public class RepBasedWorkoutItem extends WorkoutItem {
    private int reps;
    private double weight; // 0 indicates no weight.

    public RepBasedWorkoutItem(Exercise exercise, int sets, int reps, double weight) {
        super(exercise, sets);
        this.reps = reps;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }
}
