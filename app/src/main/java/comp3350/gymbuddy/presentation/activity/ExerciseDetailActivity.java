package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.services.ExerciseService;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.presentation.utils.AssetLoader;

public class ExerciseDetailActivity extends AppCompatActivity {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        TextView exerciseTitle = findViewById(R.id.exerciseTitle);
        ShapeableImageView exerciseImage = findViewById(R.id.exerciseImage);
        ChipGroup tagContainer = findViewById(R.id.tagContainer);
        LinearLayout exerciseInstructions = findViewById(R.id.exerciseInstructions);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        Intent intent = getIntent();
        ExerciseService exerciseService = new ExerciseService();
        int exerciseID = intent.getIntExtra("exerciseID", 0);
        Exercise exercise = exerciseService.getExerciseByID(exerciseID);

        exerciseTitle.setText(exercise.getName());

        var assetLoader = new AssetLoader();
        exerciseImage.setImageBitmap(assetLoader.loadImage(this, exercise.getImagePath()));

        setInstructions(exercise.getInstructions(), exerciseInstructions);

        List<Tag> tagList = exercise.getTags();

        for (int i = 0; i < tagList.size(); i++) {
            Chip chip = new Chip(this);

            chip.setText(tagList.get(i).getName());
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(tagList.get(i).getBgColor())));
            chip.setTextColor(ColorStateList.valueOf(Color.parseColor(tagList.get(i).getTextColor())));

            tagContainer.addView(chip);
        }
    }

    private void setInstructions(List<String> instructions, LinearLayout exerciseInstructions) {
        exerciseInstructions.removeAllViews(); // Clear previous instructions

        for (int i = 0; i < instructions.size(); i++) {
            String instructionText = instructions.get(i).trim();
            if (instructionText.isEmpty()) continue; // Skip empty instructions

            LinearLayout instructionLine = new LinearLayout(exerciseInstructions.getContext());
            instructionLine.setOrientation(LinearLayout.HORIZONTAL);
            instructionLine.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            TextView num = new TextView(exerciseInstructions.getContext());
            num.setText(String.valueOf(i + 1)); // Dynamically assign instruction number
            num.setBackground(ContextCompat.getDrawable(exerciseInstructions.getContext(), R.drawable.circle_background));
            num.setTextColor(Color.parseColor("#1E40AF"));
            num.setPadding(16, 8, 16, 8);

            TextView instruction = getInstruction(exerciseInstructions, instructionText);

            instructionLine.addView(num);
            instructionLine.addView(instruction);
            exerciseInstructions.addView(instructionLine);
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
