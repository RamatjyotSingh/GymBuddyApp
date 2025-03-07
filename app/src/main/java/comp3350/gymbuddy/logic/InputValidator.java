package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.logic.exception.InvalidInputException;
import comp3350.gymbuddy.logic.exception.InvalidNameException;
import comp3350.gymbuddy.logic.exception.InvalidRepsException;
import comp3350.gymbuddy.logic.exception.InvalidSetsException;
import comp3350.gymbuddy.logic.exception.InvalidTimeException;
import comp3350.gymbuddy.logic.exception.InvalidWeightException;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class InputValidator {
    public WorkoutProfile newWorkoutProfile(String name, String iconPath, List<WorkoutItem> workoutItems) {
        if (name.isEmpty()) {
            throw new InvalidNameException("Name must be provided.");
        }
        if (workoutItems == null) {
            throw new InvalidInputException("Exercises must be provided.");
        }
        if(workoutItems.isEmpty()){
            throw new InvalidInputException("Exercises must be provided.");
        }

        return new WorkoutProfile(name, iconPath, workoutItems);
    }

    public WorkoutItem newWorkoutItem(Exercise exercise, String setsField, String repsField, String weightField, String timeField) {
        int sets;
        int reps = 0;
        double weight = 0.0;
        double time = 0.0;

        // Validate sets field.
        try {
            sets = Integer.parseInt(setsField);
        } catch (NumberFormatException e) {
            throw new InvalidSetsException("Must be a valid integer.");
        }
        if (sets < 1) {
            throw new InvalidSetsException("Must be at least 1.");
        }

        if (exercise.isTimeBased()) {
            // Validate time field.
            try {
                time = Double.parseDouble(timeField);
            } catch (NumberFormatException e) {
                throw new InvalidTimeException("Must be a valid number.");
            }
            if (time < 1.0) {
                throw new InvalidTimeException("Must be at least 1.");
            }
        } else {
            // Validate reps field.
            try {
                reps = Integer.parseInt(repsField);
            } catch (NumberFormatException e) {
                throw new InvalidRepsException("Must be a valid integer.");
            }
            if (reps < 1) {
                throw new InvalidRepsException("Must be at least 1.");
            }

            if (exercise.hasWeight()) {
                // Validate weight field.
                try {
                    weight = Double.parseDouble(weightField);
                } catch (NumberFormatException e) {
                    throw new InvalidWeightException("Must be a valid number.");
                }
                if(weight < 0){
                    throw new InvalidWeightException("Must be at least 0.");
                }
            }
        }

        // Return the new workout item object.
        return new WorkoutItem(exercise, sets, reps, weight, time);
    }
}
