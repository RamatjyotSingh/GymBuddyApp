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

        binding.btnAddWorkoutItem.setOnClickListener(this::onClickBtnAddWorkoutItem);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int exerciseId = getArguments().getInt(ARG_SELECTED_EXERCISE, -1);

            // Retrieve the object we picked from exercise list
            var accessExercises = new AccessExercises();
            selectedExercise = accessExercises.getExerciseByID(exerciseId);

            updateViews();
            initializeFormValidator();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Needed to prevent memory leaks.
    }

    private void updateViews() {
        if (selectedExercise != null) {
            // Update header text in the dialog.
            binding.selectedExercise.setText(selectedExercise.getName());

            // Dynamically show or hide views based on the type of exercise.
            if (selectedExercise.isTimeBased()) {
                // Only show time.
                binding.edtReps.setVisibility(View.GONE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.edtTime.setVisibility(View.VISIBLE);
            } else {
                // Show reps.
                binding.edtReps.setVisibility(View.VISIBLE);
                binding.edtTime.setVisibility(View.GONE);

                // Show weight if applicable.
                binding.edtWeight.setVisibility(selectedExercise.hasWeight() ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void initializeFormValidator() {
        formValidator = new FormValidator(requireView());

        // Set up the constraints for the form.
        formValidator.addEditText(R.id.edtSets).validInt().greaterThan(0);
        if (selectedExercise.isTimeBased()) {
            // Make sure time is appropriate.
            formValidator.addEditText(R.id.edtTime).validDouble().greaterThan(0.0);
        } else {
            // Make sure reps & weight are appropriate.
            formValidator.addEditText(R.id.edtReps).validInt().greaterThan(0);
            if (selectedExercise.hasWeight()) {
                formValidator.addEditText(R.id.edtWeight).validDouble();
            }
        }
    }

    private @Nullable Bundle createResult() {
        WorkoutItem workoutItem = null;

        // Ensure all input is valid.
        if (formValidator.validateAll()) {
            // Extract field data and construct workout item.
            int sets = formValidator.getInt(R.id.edtSets);
            if (selectedExercise.isTimeBased()) {
                double time = formValidator.getDouble(R.id.edtTime);
                workoutItem = new TimeBasedWorkoutItem(selectedExercise, sets, time);
            } else {
                int reps = formValidator.getInt(R.id.edtReps);
                double weight = 0.0;
                if (selectedExercise.hasWeight()) {
                    weight = formValidator.getDouble(R.id.edtTime);
                }
                workoutItem = new RepBasedWorkoutItem(selectedExercise, sets, reps, weight);
            }
        }

        // Bundle the DSO for transit.
        var dsoBundler = new DSOBundler();
        return dsoBundler.bundleWorkoutItem(workoutItem);
    }

    private void onClickBtnAddWorkoutItem(View view) {
        // Create a result to pass to WorkoutBuilderActivity.
        Bundle result = createResult();

        if (result != null) {
            getParentFragmentManager().setFragmentResult("workout_item", result);
            dismiss();
        }
    }
}
