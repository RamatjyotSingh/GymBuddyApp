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

    /**
     * Returns a nicely formatted string representation of this workout item.
     * Only includes attributes with valid values.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        if (isTimeBased()) {
            // Time-based exercise formatting
            if (time > 0) {
                builder.append("Time: ").append(time).append(" sec");
            }
            
            // Add sets if present
            if (sets > 0) {
                if (builder.length() > 0) {
                    builder.append(" | ");
                }
                builder.append("Sets: ").append(sets);
            }
        } else {
            // Rep-based exercise formatting
            if (reps > 0) {
                builder.append("Reps: ").append(reps);
                
                // Add sets if present
                if (sets > 0) {
                    builder.append(" × ").append(sets).append(" sets");
                }
            } else if (sets > 0) {
                // Only sets are defined
                builder.append("Sets: ").append(sets);
            }
            
            // Add weight if present
            if (weight > 0) {
                if (builder.length() > 0) {
                    builder.append(" | ");
                }
                builder.append("Weight: ").append(weight).append(" lbs");
            }
        }
        
        return builder.toString();
    }
}
