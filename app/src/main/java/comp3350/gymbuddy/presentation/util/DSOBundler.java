package comp3350.gymbuddy.presentation.util;

import android.os.Bundle;

import androidx.annotation.Nullable;

import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;

public class DSOBundler {

    public DSOBundler() {

    }

    public @Nullable Bundle bundleWorkoutItem(WorkoutItem workoutItem) {
        Bundle result = null;

        if (workoutItem != null) {
            result = new Bundle();

            result.putInt("exerciseID", workoutItem.getExercise().getID());
            result.putInt("sets", workoutItem.getSets());

            if (workoutItem instanceof RepBasedWorkoutItem) {
                var repBasedWorkoutItem = (RepBasedWorkoutItem)workoutItem;
                result.putInt("reps", repBasedWorkoutItem.getReps());

                if (workoutItem.getExercise().hasWeight()) {
                    result.putDouble("weight", repBasedWorkoutItem.getWeight());
                }
            } else if (workoutItem instanceof TimeBasedWorkoutItem) {
                var timeBasedWorkoutItem = (TimeBasedWorkoutItem)workoutItem;
                result.putDouble("time", timeBasedWorkoutItem.getTime());
            }
        }

        return result;
    }

    public @Nullable WorkoutItem unbundleWorkoutItem(Bundle bundle) {
        WorkoutItem result = null;

        if (bundle != null) {
            int exerciseId = bundle.getInt("exerciseID", -1);
            int sets = bundle.getInt("sets", -1);
            int reps = bundle.getInt("reps", -1);
            double weight = bundle.getDouble("weight", 0.0);
            double time = bundle.getDouble("time", 0.0);

            var accessExercises = new AccessExercises();
            var exercise = accessExercises.getExerciseByID(exerciseId);

            if (sets > 0 && exercise != null) {
                if (reps > 0) {
                    result = new RepBasedWorkoutItem(exercise, sets, reps, weight);
                } else if (time > 0.0) {
                    result = new TimeBasedWorkoutItem(exercise, sets, time);
                }
            }
        }

        return result;
    }
}
