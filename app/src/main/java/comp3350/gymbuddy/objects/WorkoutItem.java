package comp3350.gymbuddy.objects;

import androidx.annotation.NonNull;

public class WorkoutItem {
    private final Exercise exercise;
    private final int sets;
    private final int reps;
    private final double weight;
    private final double time;

    /**
     * Constructor for all attributes.
     *
     * @param exercise exercise associated with this workout item.
     * @param sets number of sets to perform.
     * @param reps number of reps to perform.
     * @param weight how much weight to do.
     */
    public WorkoutItem(Exercise exercise, int sets, int reps, double weight, double time) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.time = time;
    }

    /**
     * Constructor for rep-based workout items with weight.
     *
     * @param exercise exercise associated with this workout item.
     * @param sets number of sets to perform.
     * @param reps number of reps to perform.
     * @param weight how much weight to do.
     */
    public WorkoutItem(Exercise exercise, int sets, int reps, double weight) {
        this(exercise, sets, reps, weight, 0.0);
    }

    /**
     * Constructor for time-based workout items.
     *
     * @param exercise exercise associated with this workout item.
     * @param sets number of sets to perform.
     * @param time duration in seconds for the exercise.
     */
    public WorkoutItem(Exercise exercise, int sets, double time) {
        this(exercise, sets, 0, 0.0, time);
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public double getTime() {
        return time;
    }

    public boolean isTimeBased() {
        return exercise.isTimeBased();
    }

    public boolean hasWeight() {
        return exercise.hasWeight();
    }

    @NonNull
    public String toString(){
        String result = "";

        if(isTimeBased()){
            result = "Time: " + this.time;
        }
        else{
            result = "Reps: " + this.reps + ", Weight: " + this.weight;
        }

        return result;
    }
}
