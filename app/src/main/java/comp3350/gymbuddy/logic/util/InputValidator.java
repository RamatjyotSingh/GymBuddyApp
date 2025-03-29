package comp3350.gymbuddy.logic.util;

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

/**
 * The {@code InputValidator} class provides validation methods for creating workout profiles
 * and workout items. It ensures that input fields such as name, sets, reps, weight, and time
 * meet the required constraints before constructing the corresponding objects.
 */
public class InputValidator {
    /**
     * Creates a new {@code WorkoutProfile} after validating the provided inputs.
     *
     * @param name The name of the workout profile.
     * @param iconPath The path to the workout profile icon.
     * @param workoutItems A list of workout items associated with this profile.
     * @return A new {@code WorkoutProfile} instance.
     * @throws InvalidInputException If one of the fields is invalid.
     */
    public WorkoutProfile newWorkoutProfile(String name, String iconPath, List<WorkoutItem> workoutItems) throws InvalidInputException {
        // Validate workout name.
        if (name == null || name.isEmpty()) {
            throw new InvalidNameException(ValidationMessages.invalidNameExceptionMessage);
        }

        // Validate workout items.
        if (workoutItems == null || workoutItems.isEmpty()) {
            throw new InvalidInputException(ValidationMessages.invalidInputExceptionMessage);
        }

        return new WorkoutProfile(name, iconPath, workoutItems);
    }

    /**
     * Creates a new {@code WorkoutItem} based on the provided exercise and input fields.
     *
     * @param exercise The exercise associated with the workout item.
     * @param setsField The number of sets as a string.
     * @param repsField The number of reps as a string.
     * @param weightField The weight used as a string (optional for some exercises).
     * @param timeField The time duration as a string (only for time-based exercises).
     * @return A new {@code WorkoutItem} instance.
     * @throws InvalidInputException If one of the fields is invalid.
     */
    public WorkoutItem newWorkoutItem(Exercise exercise, String setsField, String repsField, String weightField, String timeField) throws InvalidInputException {
        WorkoutItem workoutItem = null;

        if(exercise != null){
            // Extract common sets property.
            int sets = validateSetsField(setsField);

            if (exercise.isTimeBased()) {
                // Construct time-based workout item.
                double time = validateTimeField(timeField);
                workoutItem = new WorkoutItem(exercise, sets, time);
            } else {
                int reps = validateRepsField(repsField);

                if (exercise.hasWeight()) {
                    // Construct rep-based weighted workout item.
                    double weight = validateWeightField(weightField);
                    workoutItem = new WorkoutItem(exercise, sets, reps, weight);
                } else {
                    // Construct rep-based workout item.
                    workoutItem = new WorkoutItem(exercise, sets, reps);
                }
            }
        }
        else{
            throw new InvalidInputException(ValidationMessages.invalidInputExceptionMessage);
        }

        // Return the new workout item object.
        return workoutItem;
    }

    /**
     * Validates the sets field and converts it to an integer.
     *
     * @param setsField The sets input string.
     * @return The validated sets value.
     * @throws InvalidSetsException If the input is not a valid integer or less than 1.
     */
    private int validateSetsField(String setsField) throws InvalidSetsException {
        int sets;

        // Validate sets field.
        try {
            sets = Integer.parseInt(setsField);
        } catch (NumberFormatException e) {
            throw new InvalidSetsException(ValidationMessages.integerFormatExceptionMessage);
        }
        if (sets < 1) {
            throw new InvalidSetsException(ValidationMessages.invalidNonzeroValueMessage);
        }

        return sets;
    }

    /**
     * Validates the time field and converts it to a double.
     *
     * @param timeField The time input string.
     * @return The validated time value.
     * @throws InvalidTimeException If the input is not a valid number or less than 1.0.
     */
    private double validateTimeField(String timeField) throws InvalidTimeException {
        double time;

        // Validate time field.
        try {
            time = Double.parseDouble(timeField);
        } catch (NumberFormatException e) {
            throw new InvalidTimeException(ValidationMessages.doubleFormatExceptionMessage);
        }
        if (time < 1.0) {
            throw new InvalidTimeException(ValidationMessages.invalidNonnegativeValueMessage);
        }

        return time;
    }

    /**
     * Validates the reps field and converts it to an integer.
     *
     * @param repsField The reps input string.
     * @return The validated reps value.
     * @throws InvalidRepsException If the input is not a valid integer or less than 1.
     */
    private int validateRepsField(String repsField) throws InvalidRepsException {
        int reps;

        // Validate reps field.
        try {
            reps = Integer.parseInt(repsField);
        } catch (NumberFormatException e) {
            throw new InvalidRepsException(ValidationMessages.integerFormatExceptionMessage);
        }
        if (reps < 1) {
            throw new InvalidRepsException(ValidationMessages.invalidNonzeroValueMessage);
        }

        return reps;
    }

    /**
     * Validates the weight field and converts it to a double.
     *
     * @param weightField The weight input string.
     * @return The validated weight value.
     * @throws InvalidWeightException If the input is not a valid number or less than 0.
     */
    private double validateWeightField(String weightField) throws InvalidWeightException {
        double weight;

        // Validate weight field.
        try {
            weight = Double.parseDouble(weightField);
        } catch (NumberFormatException e) {
            throw new InvalidWeightException(ValidationMessages.doubleFormatExceptionMessage);
        }
        if (weight < 0) {
            throw new InvalidWeightException(ValidationMessages.invalidNonnegativeValueMessage);
        }

        return weight;
    }
}
