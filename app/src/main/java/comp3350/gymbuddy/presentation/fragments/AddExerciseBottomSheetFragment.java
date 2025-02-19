package comp3350.gymbuddy.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
public class AddExerciseBottomSheetFragment extends BottomSheetDialogFragment {

    private Exercise exercise;

    public AddExerciseBottomSheetFragment(Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_exercise_bottom_sheet, container, false);

        TextView textViewExerciseName = view.findViewById(R.id.textViewExerciseName);
        Button btnAddExercise = view.findViewById(R.id.btnAddExercise);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        textViewExerciseName.setText(exercise.getName());

        btnAddExercise.setOnClickListener(v -> {
            // Send exercise data back to the previous activity (WorkoutBuilderActivity)
            Intent resultIntent = new Intent();
            resultIntent.putExtra("exerciseID", exercise.getID());

            // Set the result and close the BottomSheet
            requireActivity().setResult(Activity.RESULT_OK, resultIntent);
            requireActivity().finish();
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());



        return view;
    }
}
