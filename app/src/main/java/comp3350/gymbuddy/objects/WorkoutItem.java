package comp3350.gymbuddy.objects;

public class WorkoutItem {
    private Exercise exercise;
    private int sets;

    public WorkoutItem(Exercise exercise, int sets) {
        this.exercise = exercise;
        this.sets = sets;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }
}
