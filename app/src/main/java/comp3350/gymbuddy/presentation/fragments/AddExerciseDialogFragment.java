package comp3350.gymbuddy.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
                binding.edtReps.setVisibility(View.INVISIBLE);
                binding.edtWeight.setVisibility(View.INVISIBLE);
                binding.edtTime.setVisibility(View.VISIBLE);
            } else {
                // Show reps.
                binding.edtReps.setVisibility(View.VISIBLE);
                binding.edtTime.setVisibility(View.INVISIBLE);

                // Show weight if applicable.
                binding.edtWeight.setVisibility(selectedExercise.hasWeight() ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    private @Nullable Bundle createResult() {
        WorkoutItem workoutItem = null;

        var validator = new FormValidator();

        int sets = validator.nextInt(binding.edtSets, -1);

        if (selectedExercise.isTimeBased()) {
            double time = validator.nextDouble(binding.edtTime, 0.0);

            if (validator.getValid()) {
                workoutItem = new TimeBasedWorkoutItem(selectedExercise, sets, time);
            }
        } else {
            int reps = validator.nextInt(binding.edtReps, -1);

            double weight = 0.0;
            if (selectedExercise.hasWeight()) {
                weight = validator.nextDouble(binding.edtWeight, 0.0);
            }

            if (validator.getValid()) {
                workoutItem = new RepBasedWorkoutItem(selectedExercise, sets, reps, weight);
            }
        }

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
