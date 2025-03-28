package comp3350.gymbuddy.presentation.util;

import android.os.Bundle;

import androidx.annotation.Nullable;

import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import timber.log.Timber;

public class DSOBundler {
    private static final String TAG = "DSOBundler";
   
    /**
     * Converts a WorkoutItem into a Bundle for easy storage and transfer.
     *
     * @param workoutItem The WorkoutItem to be bundled.
     * @return A Bundle containing the workout item data, or null if workoutItem is null.
     */
    public @Nullable Bundle bundleWorkoutItem(WorkoutItem workoutItem)  {
        Bundle result = null;

        if (workoutItem != null) {
            result = new Bundle();

            // Store basic workout item properties
            result.putInt("exerciseID", workoutItem.getExercise().getID());
            result.putInt("sets", workoutItem.getSets());

            // Store additional properties based on workout item type
            if (workoutItem.isTimeBased()) {
                result.putDouble("time", workoutItem.getTime());
            } else {
                result.putInt("reps", workoutItem.getReps());

                // Only store weight if the exercise involves weights
                if (workoutItem.getExercise().hasWeight()) {
                    result.putDouble("weight", workoutItem.getWeight());
                }
            }
        }

        return result;
    }

    /**
     * Reconstructs a WorkoutItem from a given Bundle.
     *
     * @param bundle The Bundle containing workout item data.
     * @return A WorkoutItem object reconstructed from the bundle, or null if invalid data is found.
     */
    public @Nullable WorkoutItem unbundleWorkoutItem(Bundle bundle)  {
        WorkoutItem result = null;

        if (bundle != null) {
          
                // Extract stored values from the bundle
                int exerciseId = bundle.getInt("exerciseID", -1);
                int sets = bundle.getInt("sets", -1);
                int reps = bundle.getInt("reps", -1);
                double weight = bundle.getDouble("weight", 0.0);
                double time = bundle.getDouble("time", 0.0);

                // Retrieve the exercise object using ApplicationService
                ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();
                Exercise exercise = exerciseManager.getExerciseByID(exerciseId);

                if (exercise != null) {
                    result = new WorkoutItem(exercise, sets, reps, weight, time);
                } else {
                    Timber.tag(TAG).e("Failed to unbundle WorkoutItem: Exercise with ID %d not found", exerciseId);
                }
           
        }

        return result;
    }
}