package comp3350.gymbuddy.objects;

import androidx.annotation.NonNull;

public class RepBasedSessionItem extends SessionItem {
    private final double weight;
    private final int reps;

    public RepBasedSessionItem(WorkoutItem associatedWorkoutItem, double weight, int reps) {
        super(associatedWorkoutItem);
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight(){ return this.weight; }

    public int getReps() { return this.reps; }

    @NonNull
    public String toString(){
        return getReps() + " reps, " + getWeight() + " lbs";
    }
}
