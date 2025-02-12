package comp3350.gymbuddy.objects;

public class Set {
    private double weight;
    private int repetitions;
    private double time;
    private WorkoutItem associatedWorkoutItem;

    public Set(double weight, int repetitions, double time, WorkoutItem associatedWorkoutItem){
        this.weight = weight;
        this.repetitions = repetitions;
        this.time = time;
        this.associatedWorkoutItem = associatedWorkoutItem;
    }
}
