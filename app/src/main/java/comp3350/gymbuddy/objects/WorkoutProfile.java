package comp3350.gymbuddy.objects;

import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;

import java.util.Collections;
import java.util.List;

public class WorkoutProfile {
    private String name;
    private String iconPath;
    private List<WorkoutItem> workoutItems;

    public WorkoutProfile(String name, String iconPath, List<WorkoutItem> workoutItems) {
        this.name = name;
        this.iconPath = iconPath;
        this.workoutItems = workoutItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath(){ return this.iconPath; }

    public List<WorkoutItem> getWorkoutItems(){ return Collections.unmodifiableList(workoutItems); }
}
