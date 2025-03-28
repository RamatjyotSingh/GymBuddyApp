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
import comp3350.gymbuddy.logic.ApplicationService;

import timber.log.Timber;

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
        int exerciseID = intent.getIntExtra(getString(R.string.exerciseid), 0);

        try {
            // Get the exercise manager from ApplicationService
            ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();
            
            // Get the exercise details using the manager
            Exercise exercise = exerciseManager.getExerciseByID(exerciseID);
            
            // Update the views with exercise info
            setExercise(exercise);
        } catch (IllegalStateException e) {
            // This happens if ApplicationService isn't initialized
            Toast.makeText(this, "Application not properly initialized. Please restart the app.", Toast.LENGTH_LONG).show();
            Timber.e(e, "ApplicationService not initialized when accessing ExerciseDetailActivity");
            finish(); // Close the activity as it can't function properly
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load exercise details: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Timber.e(e, "Error loading exercise details");
        }
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

        // Replace escape sequences with actual newlines
        String processedInstructions = instructions.replace("\\n", "\n");
        
        // Now split by the actual newlines
        List<String> lines = processedInstructions.lines().toList();

        // Process each instruction line
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
                num.setTextColor(Color.parseColor(getString(R.string.instruction_num_color)));
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
