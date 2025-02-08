package comp3350.gymbuddy.objects;

public class WorkoutItem {
    private Exercise exercise;
    private Double weight;
    private Integer repetitions;
    private Double time;

    public WorkoutItem(Exercise exercise, Double weight, Integer repetitions, Double time) {
        this.exercise = exercise;
        this.weight = weight;
        this.repetitions = repetitions;
        this.time = time;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Double getTime() {
        return time;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
