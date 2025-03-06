package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityExerciseDetailBinding;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.util.AssetLoader;

public class ExerciseDetailActivity extends AppCompatActivity {

    // View binding for accessing UI elements efficiently
    private ActivityExerciseDetailBinding binding;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using view binding
        binding = ActivityExerciseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the passed exercise ID.
        Intent intent = getIntent();
        int exerciseID = intent.getIntExtra("exerciseID", 0);

        // Get the exercise details from persistence.
        var exerciseManager = new ExerciseManager(true);
        Exercise exercise = null;
        try {
            exercise = exerciseManager.getExerciseByID(exerciseID);
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Update the views with exercise info.
        setExercise(exercise);
    }

    private void setExercise(Exercise exercise) {
        if (exercise != null) {
            // Update the title.
            binding.exerciseTitle.setText(exercise.getName());

            var assetLoader = new AssetLoader();
            binding.exerciseImage.setImageBitmap(assetLoader.loadImage(this, exercise.getImagePath()));

            setInstructions(exercise.getInstructions());

            List<Tag> tagList = exercise.getTags();

            for (int i = 0; i < tagList.size(); i++) {
                Chip chip = new Chip(this);

                chip.setText(tagList.get(i).getName());
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(tagList.get(i).getBgColor())));
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor(tagList.get(i).getTextColor())));

                binding.tagContainer.addView(chip);
            }
        }
    }

    private void setInstructions(String instructions) {
        binding.exerciseInstructions.removeAllViews(); // Clear previous instructions

        List<String> lines = instructions.lines().toList();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (!line.isEmpty()) {
                LinearLayout instructionLine = new LinearLayout(binding.exerciseInstructions.getContext());
                instructionLine.setOrientation(LinearLayout.HORIZONTAL);
                instructionLine.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                TextView num = new TextView(binding.exerciseInstructions.getContext());
                num.setText(String.valueOf(i + 1)); // Dynamically assign instruction number
                num.setBackground(ContextCompat.getDrawable(binding.exerciseInstructions.getContext(), R.drawable.circle_background));
                num.setTextColor(Color.parseColor("#1E40AF"));
                num.setPadding(16, 8, 16, 8);

                TextView instruction = getInstruction(binding.exerciseInstructions, line);

                instructionLine.addView(num);
                instructionLine.addView(instruction);
                binding.exerciseInstructions.addView(instructionLine);
            }
        }
    }

    @NonNull
    private static TextView getInstruction(LinearLayout exerciseInstructions, String instructionText) {
        TextView instruction = new TextView(exerciseInstructions.getContext());
        instruction.setText(instructionText);
        instruction.setTextSize(18);

        LinearLayout.LayoutParams instructionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        instructionParams.setMargins(16, 8, 16, 8);
        instruction.setLayoutParams(instructionParams);
        return instruction;
    }
}
