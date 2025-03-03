package comp3350.gymbuddy.objects;

import androidx.annotation.NonNull;

public class TimeBasedSessionItem extends SessionItem {
    private final double time;

    public TimeBasedSessionItem(WorkoutItem associatedWorkoutItem, double time) {
        super(associatedWorkoutItem);
        this.time = time;
    }

    public double getTime(){ return this.time; }

    @NonNull
    public String toString(){
        return getTime() + " sec";
    }
}
