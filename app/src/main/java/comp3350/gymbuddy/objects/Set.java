package comp3350.gymbuddy.objects;

public class Set {
    private Double weight;
    private Integer repetitions;
    private Double time;
    private WorkoutItem associatedWorkoutItem;

    public Set(Double weight, Integer repetitions, Double time, WorkoutItem associatedWorkoutItem){
        this.weight = weight;
        this.repetitions = repetitions;
        this.time = time;
        this.associatedWorkoutItem = associatedWorkoutItem;
    }

    public Double getWeight() { return this.weight; }
    public Integer getRepetitions() { return this.repetitions; }
    public Double getTime() { return this.time; }
    public WorkoutItem getAssociatedWorkoutItem() { return this.associatedWorkoutItem; }

    public void setWeight(Double weight) { this.weight = weight; }
    public void setRepetitions(Integer repetitions) { this.repetitions = repetitions; }
    public void setTime(Double time) { this.time = time; }
    public void setAssociatedWorkoutItem(WorkoutItem associatedWorkoutItem) { this.associatedWorkoutItem = associatedWorkoutItem; }
}
