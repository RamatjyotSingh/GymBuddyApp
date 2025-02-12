package comp3350.gymbuddy.objects;

public class WorkoutItem {
    private Exercise exercise;
    private int sets;
    private int reps;
    private double weight; // 0 indicates no weight.
    private double time; // 0 indicates no time.

    public WorkoutItem(Exercise exercise, int sets, int reps, double weight, double time) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.time = time;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public double getTime() {
        return time;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }
}
