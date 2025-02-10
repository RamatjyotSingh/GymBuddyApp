package comp3350.gymbuddy.objects;

import java.util.List;

public class WorkoutProfile {
    private String name;
    private String iconPath;
    private List<WorkoutItem> workoutItems;

    public WorkoutProfile(String name, String icon_path, List<WorkoutItem> workout_items) {
        this.name = name;
        this.iconPath = icon_path;
        this.workoutItems = workout_items;
    }

    public String getName() {
        return name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public List<WorkoutItem> getExercises() {
        return workoutItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setExercises(List<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }
}
