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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.R;


public class AddExerciseBottomSheetFragment extends BottomSheetDialogFragment {

    private final Exercise exercise;

    public AddExerciseBottomSheetFragment(Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



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

    @Override
    public void onStart() {
        super.onStart();

        // Force BottomSheet to expand fully
        if (getDialog() instanceof BottomSheetDialog) {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) getDialog();
            View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }


    }

}
