package comp3350.gymbuddy.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.DialogAddWorkoutItemBinding;
import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.presentation.util.DSOBundler;
import comp3350.gymbuddy.presentation.util.FormValidator;

public class AddExerciseDialogFragment extends DialogFragment {
    private static String ARG_SELECTED_EXERCISE = "selected_exercise";
    private DialogAddWorkoutItemBinding binding;

    private Exercise selectedExercise;
    private FormValidator formValidator;

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
            var accessExercises = new AccessExercises();
            selectedExercise = accessExercises.getExerciseByID(exerciseId);

            updateViews();
            initializeFormValidator();
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
     * Initializes form validation for user input fields.
     */
    private void initializeFormValidator() {
        formValidator = new FormValidator(requireView());

        // Ensure sets field is a valid integer greater than 0
        formValidator.addEditText(R.id.edtSets).validInt().greaterThan(0);

        if (selectedExercise.isTimeBased()) {
            // Validate time field for time-based exercises
            formValidator.addEditText(R.id.edtTime).validDouble().greaterThan(0.0);
        } else {
            // Validate reps for rep-based exercises
            formValidator.addEditText(R.id.edtReps).validInt().greaterThan(0);

            // Validate weight if applicable
            if (selectedExercise.hasWeight()) {
                formValidator.addEditText(R.id.edtWeight).validDouble();
            }
        }
    }

    /**
     * Creates a WorkoutItem based on user input and returns it in a Bundle.
     * Returns null if validation fails.
     */
    private @Nullable Bundle createResult() {
        WorkoutItem workoutItem = null;

        // Ensure all input is valid before proceeding
        if (formValidator.validateAll()) {
            int sets = formValidator.getInt(R.id.edtSets);

            if (selectedExercise.isTimeBased()) {
                // Create a time-based workout item
                double time = formValidator.getDouble(R.id.edtTime);
                workoutItem = new TimeBasedWorkoutItem(selectedExercise, sets, time);
            } else {
                // Create a rep-based workout item
                int reps = formValidator.getInt(R.id.edtReps);
                double weight = 0.0;

                if (selectedExercise.hasWeight()) {
                    weight = formValidator.getDouble(R.id.edtWeight);
                }

                workoutItem = new RepBasedWorkoutItem(selectedExercise, sets, reps, weight);
            }
        }

        // Convert the workout item to a Bundle for passing back
        var dsoBundler = new DSOBundler();
        return dsoBundler.bundleWorkoutItem(workoutItem);
    }

    /**
     * Handles the add button click event, validates input, and returns result.
     */
    private void onClickBtnAddWorkoutItem(View view) {
        Bundle result = createResult();

        if (result != null) {
            // Send result back to WorkoutBuilderActivity
            getParentFragmentManager().setFragmentResult("workout_item", result);
            dismiss();
        }
    }
}