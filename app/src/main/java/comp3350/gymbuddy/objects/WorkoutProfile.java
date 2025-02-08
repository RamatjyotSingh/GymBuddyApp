package comp3350.gymbuddy.objects;

import java.util.List;

public class WorkoutProfile {
    private String name;
    private String iconPath;
    private List<Exercise> exercises;

    public WorkoutProfile(String name, String icon_path, List<Exercise> exercises) {
        this.name = name;
        this.iconPath = icon_path;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
