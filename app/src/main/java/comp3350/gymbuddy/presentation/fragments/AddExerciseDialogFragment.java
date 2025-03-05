package comp3350.gymbuddy.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import comp3350.gymbuddy.databinding.DialogAddWorkoutItemBinding;
import comp3350.gymbuddy.logic.services.ExerciseService;
import comp3350.gymbuddy.logic.InputValidator;
import comp3350.gymbuddy.logic.exception.InvalidRepsException;
import comp3350.gymbuddy.logic.exception.InvalidSetsException;
import comp3350.gymbuddy.logic.exception.InvalidTimeException;
import comp3350.gymbuddy.logic.exception.InvalidWeightException;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.presentation.utils.DSOBundler;

public class AddExerciseDialogFragment extends DialogFragment {
    private static final String ARG_SELECTED_EXERCISE = "selected_exercise";
    private DialogAddWorkoutItemBinding binding;
    private Exercise selectedExercise;

    /**
     * Creates a new instance of the dialog with the selected exercise ID.
     */
    public static AddExerciseDialogFragment newInstance(int selectedExerciseId) {
        var dialog = new AddExerciseDialogFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SELECTED_EXERCISE, selectedExerciseId);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddWorkoutItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set up button click listener
        binding.btnAddWorkoutItem.setOnClickListener(this::onClickBtnAddWorkoutItem);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int exerciseId = getArguments().getInt(ARG_SELECTED_EXERCISE, -1);

            // Retrieve the selected exercise from storage
            var accessExercises = new ExerciseService();
            selectedExercise = accessExercises.getExerciseByID(exerciseId);

            updateViews();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks by clearing the binding reference
    }

    /**
     * Updates the UI based on the selected exercise type.
     */
    private void updateViews() {
        if (selectedExercise != null) {
            // Set exercise name as the dialog header
            binding.selectedExercise.setText(selectedExercise.getName());

            // Adjust visible input fields based on exercise type
            if (selectedExercise.isTimeBased()) {
                binding.edtReps.setVisibility(View.GONE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.edtTime.setVisibility(View.VISIBLE);
            } else {
                binding.edtReps.setVisibility(View.VISIBLE);
                binding.edtTime.setVisibility(View.GONE);

                // Show weight input field only if required by the exercise
                binding.edtWeight.setVisibility(selectedExercise.hasWeight() ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * Creates a WorkoutItem based on user input and returns it.
     * Returns null if validation fails.
     */
    private @Nullable WorkoutItem generateWorkoutItem() {
        WorkoutItem workoutItem = null;

        // Ensure all input is valid before proceeding
        try {
            String sets = binding.edtSets.getText().toString();
            String reps = binding.edtReps.getText().toString();
            String weight = binding.edtWeight.getText().toString();
            String time = binding.edtTime.getText().toString();

            var inputValidator = new InputValidator();
            workoutItem = inputValidator.newWorkoutItem(
                    selectedExercise,
                    sets,
                    reps,
                    weight,
                    time
            );

            // Report invalid text input.
        } catch (InvalidSetsException e) {
            binding.edtSets.setError(e.getMessage());
        } catch (InvalidRepsException e) {
            binding.edtReps.setError(e.getMessage());
        } catch (InvalidWeightException e) {
            binding.edtWeight.setError(e.getMessage());
        } catch (InvalidTimeException e) {
            binding.edtTime.setError(e.getMessage());
        }

        return workoutItem;
    }

    /**
     * Handles the add button click event, validates input, and returns result.
     */
    private void onClickBtnAddWorkoutItem(View view) {
        WorkoutItem workoutItem = generateWorkoutItem();

        if (workoutItem != null) {
            // Convert the workout item to a Bundle for passing back
            DSOBundler bundler = new DSOBundler();
            Bundle result = bundler.bundleWorkoutItem(workoutItem);

            if (result != null) {
                // Send result back to WorkoutBuilderActivity
                getParentFragmentManager().setFragmentResult("workout_item", result);
                dismiss();
            }
        }
    }
}