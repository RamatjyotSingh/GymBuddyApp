package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import comp3350.gymbuddy.databinding.ActivityWorkoutPlayerBinding;
import comp3350.gymbuddy.logic.util.StringFormatter;
import comp3350.gymbuddy.logic.util.WorkoutPlaybackController;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import timber.log.Timber;

public class WorkoutPlayerActivity extends Activity {

    private WorkoutPlaybackController controller;

    // View binding for accessing UI elements efficiently
    private ActivityWorkoutPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using view binding
        binding = ActivityWorkoutPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the workout ID passed through the intent.
        int id = getIntent().getIntExtra("workoutProfileId", 0);

        // Fetch the profile from the database.
        WorkoutProfile workoutProfile = null;
        try {
            WorkoutManager workoutManager = ApplicationService.getInstance().getWorkoutManager();
            workoutProfile = workoutManager.getWorkoutProfileByID(id);
            
            controller = null;

            if (workoutProfile != null) {
                // If success, start the workout.
                startWorkout(workoutProfile);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Application not properly initialized. Please restart the app.", Toast.LENGTH_LONG).show();
            Timber.e(e, "ApplicationService not initialized in WorkoutPlayerActivity");
            finish();
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Timber.e(e, "Database error in WorkoutPlayerActivity: %s", e.getMessage());
            finish();
        }
    }

    /**
     * Does all the setup required to start a workout for a valid workout profile.
     * @param profile the workout profile containing the workout.
     */
    private void startWorkout(WorkoutProfile profile) {
        // Populate the workout name.
        binding.tvWorkoutName.setText(profile.getName());

        // Create the controller.
        controller = new WorkoutPlaybackController(profile, this::onBeginWorkoutItem, this::onFinishedWorkout);
        controller.startWorkout();
    }

    /**
     * Handles the Floating Action Button (FAB) click event.
     * Creates a new entry in the recycler view.fs
     */
    public void onClickFAB(View v) {

    }

    /**
     * Handles the Previous button click event.
     */
    public void onClickPrevious(View v) {

    }

    /**
     * Handles the Next button click event.
     */
    public void onClickNext(View v) {
        controller.proceed();
    }

    /**
     * Gets called when the controller has switched to a different workout item.
     * @param workoutItem the new item to display.
     */
    private void onBeginWorkoutItem(WorkoutItem workoutItem) {
        // Update the views.
        updateWorkoutItemInfo(workoutItem);
    }

    /**
     * Gets called when the workout is complete.
     * @param workoutSession the new workout session
     */
    private void onFinishedWorkout(WorkoutSession workoutSession) {
        // Go back to the main screen.
        finish();
    }

    /**
     * Updates the views to reflect the information in the workout item object.
     * @param workoutItem the workout item to display.
     */
    private void updateWorkoutItemInfo(WorkoutItem workoutItem) {
        // Set the exercise name.
        binding.tvExerciseName.setText(workoutItem.getExercise().getName());

        String sets = Integer.toString(workoutItem.getSets());
        String time = null, reps = null, weight = null;

        if (workoutItem.isTimeBased()) {
            var formatter = new StringFormatter();
            time = formatter.formatTime(workoutItem.getTime());
        } else {
            reps = Integer.toString(workoutItem.getReps());

            if (workoutItem.hasWeight()) {
                var formatter = new StringFormatter();
                weight = formatter.formatWeight(workoutItem.getWeight());
            }
        }

        setExerciseHeaders(sets, time, reps, weight);
    }

    /**
     * Sets the text labels for each of the exercise headers.
     * @param sets the text to show for the number of sets.
     * @param time the text to show for the time.
     * @param reps the text to show for the number of reps.
     * @param weight the text to show for the amount of weight.
     */
    private void setExerciseHeaders(String sets, String time, String reps, String weight) {
        // Update sets views.
        if (sets != null) {
            binding.setsLayout.setVisibility(View.VISIBLE);
            binding.tvSets.setText(sets);
        } else {
            binding.setsLayout.setVisibility(View.GONE);
        }

        // Update time views.
        if (time != null) {
            binding.timeLayout.setVisibility(View.VISIBLE);
            binding.tvTime.setText(time);
        } else {
            binding.timeLayout.setVisibility(View.GONE);
        }

        // Update reps views.
        if (reps != null) {
            binding.repsLayout.setVisibility(View.VISIBLE);
            binding.tvReps.setText(reps);
        } else {
            binding.repsLayout.setVisibility(View.GONE);
        }

        // Update weight views.
        if (weight != null) {
            binding.weightLayout.setVisibility(View.VISIBLE);
            binding.tvWeight.setText(weight);
        } else {
            binding.weightLayout.setVisibility(View.GONE);
        }
    }
}
