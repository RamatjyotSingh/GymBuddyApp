package comp3350.gymbuddy.objects;

import comp3350.gymbuddy.objects.Difficulty;

public class Exercise {
    private String name;
    private Difficulty diff;
    private String[] primaryMuscles;
    private String[] secondaryMuscles;
    private String instructions;

    public Exercise(){
        this.name = null;
        this.diff = null;
        this.primaryMuscles = null;
        this.secondaryMuscles = null;
        this.instructions = null;
    }

    public Exercise(String name, Difficulty diff, String[] primaryMuscles, String[] secondaryMuscles, String instructions){
        this.name = name;
        this.diff = diff;
        this.primaryMuscles = primaryMuscles;
        this.secondaryMuscles = secondaryMuscles;
        this.instructions = instructions;
    }
}
