package comp3350.gymbuddy.logic.util;

import java.util.ArrayList;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;

public class WorkoutPlaybackController {
    public interface BeginWorkoutItemListener {
        void onBeginWorkoutItem(WorkoutItem workoutItem);
    }

    public interface FinishWorkoutListener {
        void onFinishedWorkout(WorkoutSession workoutSession);
    }

    private final WorkoutProfile workoutProfile;
    private int currentIndex;
    private final BeginWorkoutItemListener beginWorkoutItemListener;
    private final FinishWorkoutListener finishWorkoutListener;

    public WorkoutPlaybackController(WorkoutProfile profile, BeginWorkoutItemListener beginWIListener, FinishWorkoutListener finishWorkoutListener) {
        this.workoutProfile = profile;
        this.beginWorkoutItemListener = beginWIListener;
        this.finishWorkoutListener = finishWorkoutListener;
    }

    public void startWorkout() {
        // Start with the first item.
        setActiveWorkoutItem(0);
    }

    public void proceed() {
        // Continue if there's more exercises.
        if (currentIndex < workoutProfile.getWorkoutItems().size() - 1) {
            setActiveWorkoutItem(currentIndex+1);
        } else {
            // Otherwise, finish.
            WorkoutSession workoutSession = new WorkoutSession(555, 55, 66, new ArrayList<>(), workoutProfile); // TODO: fix this!
            finishWorkoutListener.onFinishedWorkout(workoutSession);
        }
    }

    private void setActiveWorkoutItem(int index) {
        currentIndex = index;
        var workoutItem = workoutProfile.getWorkoutItems().get(currentIndex);
        beginWorkoutItemListener.onBeginWorkoutItem(workoutItem);
    }
}
