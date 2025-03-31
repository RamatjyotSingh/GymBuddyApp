package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityExerciseDetailBinding;
import comp3350.gymbuddy.logic.exception.BusinessException;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.presentation.util.ErrorHandler;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.presentation.util.AssetLoader;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.util.StringFormatter;

import comp3350.gymbuddy.presentation.util.ToastErrorDisplay;

public class ExerciseDetailActivity extends AppCompatActivity {

    // View binding for accessing UI elements efficiently
    private ActivityExerciseDetailBinding binding;
    private final ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(this));

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
        int exerciseID = intent.getIntExtra(getString(R.string.exerciseid), -1);

        // Handle invalid exercise passed.
        if (exerciseID == -1) {
            handler.handle(new IllegalArgumentException("No exercise ID passed to ExerciseDetailActivity"), handler.getDefaultErrorMessage());
            finish();
        }

        try {
            // Get the exercise details using the manager
            ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();
            Exercise exercise = exerciseManager.getExerciseByID(exerciseID);

            // Update the views with exercise info
            setExercise(exercise);
        } catch (BusinessException e) {
            handler.handle(e,getString(R.string.error_loading_exercise));
            finish();
        }
    }

    /**
     * Sets the views for an exercise.
     * @param exercise the exercise to get information from.
     */
    private void setExercise(Exercise exercise) {
        if (exercise != null) {
            // Update the title.
            binding.exerciseTitle.setText(exercise.getName());

            // Load the image.
            var assetLoader = new AssetLoader();
            binding.exerciseImage.setImageBitmap(assetLoader.loadImage(this, exercise.getImagePath()));

            // Set the instructions
            setInstructions(exercise.getInstructions());

            // Set up the tags
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

    /**
     * Takes a string of instructions and applies them to the screen.
     * @param instructions newline separated instructions
     */
    private void setInstructions(String instructions) {
        binding.exerciseInstructions.removeAllViews();
        
        // Use the StringFormatter to get clean instruction list
        StringFormatter formatter = new StringFormatter();
        List<String> formattedInstructions = formatter.formatInstructions(instructions);
        
        // Now just focus on rendering the UI elements
        for (int i = 0; i < formattedInstructions.size(); i++) {
            String line = formattedInstructions.get(i);

            // Create the layout for the instruction
            LinearLayout instructionLine = new LinearLayout(this);
            instructionLine.setOrientation(LinearLayout.HORIZONTAL);
            instructionLine.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            // Create the text view
            TextView num = new TextView(this);
            num.setText(String.valueOf(i + 1));
            num.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_background));
            num.setTextColor(Color.parseColor(getString(R.string.instruction_num_color)));
            num.setPadding(16, 8, 16, 8);

            // Create the child view
            TextView instruction = getInstruction(binding.exerciseInstructions, line);

            // Add the instruction
            instructionLine.addView(num);
            instructionLine.addView(instruction);
            binding.exerciseInstructions.addView(instructionLine);
        }
    }

    /**
     * Creates the textview for a specified instruction.
     * @param exerciseInstructions the target layout
     * @param instructionText the text of the intended instruction
     * @return the text view
     */
    @NonNull
    private static TextView getInstruction(LinearLayout exerciseInstructions, String instructionText) {
        // Create the textview object
        TextView instruction = new TextView(exerciseInstructions.getContext());
        instruction.setText(instructionText);
        instruction.setTextSize(18);

        // Set up the appearance.
        LinearLayout.LayoutParams instructionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        instructionParams.setMargins(16, 8, 16, 8);
        instruction.setLayoutParams(instructionParams);

        return instruction;
    }
}
