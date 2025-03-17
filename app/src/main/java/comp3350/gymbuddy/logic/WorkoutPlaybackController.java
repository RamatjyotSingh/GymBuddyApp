package comp3350.gymbuddy.logic;

import java.util.ArrayList;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;

public class WorkoutPlaybackController {
    public interface BeginWorkoutItemListener {
        void onBeginWorkoutItem(WorkoutItem workoutItem);
    }

    public interface FinishWorkoutListener {
        void onFinishedWorkout(WorkoutSession workoutSession);
    }

    private WorkoutProfile workoutProfile;
    private int currentIndex;
    private BeginWorkoutItemListener beginWorkoutItemListener;
    private FinishWorkoutListener finishWorkoutListener;

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
        if (currentIndex < workoutProfile.getWorkoutItems().size()) {
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
