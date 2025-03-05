package comp3350.gymbuddy.objects;

public class SessionItem {
    private final WorkoutItem associatedWorkoutItem;
    private final int reps;
    private final double weight;
    private final double time;

    /**
     * Constructor for all attributes.
     *
     * @param associatedWorkoutItem workout item associated with this session item.
     * @param reps number of reps to perform.
     * @param weight how much weight to do.
     */
    public SessionItem(WorkoutItem associatedWorkoutItem, int reps, double weight, double time) {
        this.associatedWorkoutItem = associatedWorkoutItem;
        this.reps = reps;
        this.weight = weight;
        this.time = time;
    }

    /**
     * Constructor for rep-based workout items with weight.
     *
     * @param associatedWorkoutItem workout item associated with this session item.
     * @param reps number of reps to perform.
     * @param weight how much weight to do.
     */
    public SessionItem(WorkoutItem associatedWorkoutItem, int reps, double weight) {
        this(associatedWorkoutItem, reps, weight, 0.0);
    }

    /**
     * Constructor for time-based workout items.
     *
     * @param associatedWorkoutItem workout item associated with this session item.
     * @param time duration in seconds for the exercise.
     */
    public SessionItem(WorkoutItem associatedWorkoutItem, double time) {
        this(associatedWorkoutItem, 0, 0.0, time);
    }

    public WorkoutItem getAssociatedWorkoutItem() {
        return associatedWorkoutItem;
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
}
