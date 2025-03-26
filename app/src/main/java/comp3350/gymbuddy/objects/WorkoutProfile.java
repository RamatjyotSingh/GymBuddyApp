package comp3350.gymbuddy.objects;

import java.util.Collections;
import java.util.List;

public class WorkoutProfile {
    private int id;
    private String name;
    private final String iconPath;
    private final List<WorkoutItem> workoutItems;
    private final boolean isDeleted;

    // Constructor for creating a new profile (before saving to DB), also use with stubs
    public WorkoutProfile(String name, String iconPath, List<WorkoutItem> workoutItems) {
        this.id = -1; // Default value before being assigned by the database
        this.name = name;
        this.iconPath = iconPath;
        this.workoutItems = workoutItems;
        this.isDeleted = false;
    }

    // Constructor for loading from DB (with an existing ID)
    public WorkoutProfile(int id, String name, String iconPath, List<WorkoutItem> workoutItems, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.iconPath = iconPath;
        this.workoutItems = workoutItems;
        this.isDeleted = isDeleted;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconPath() {
        return this.iconPath;
    }

    public List<WorkoutItem> getWorkoutItems() {
        return Collections.unmodifiableList(workoutItems);
    }
}
