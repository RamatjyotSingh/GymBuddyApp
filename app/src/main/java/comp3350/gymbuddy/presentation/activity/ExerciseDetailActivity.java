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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comp3350.gymbuddy.R;

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
        String title = intent.getStringExtra("title");
        String imagePath = intent.getStringExtra("imagePath");
        ArrayList<String> instructions = intent.getStringArrayListExtra("instructions");
        ArrayList<String> tagNames = intent.getStringArrayListExtra("tagNames");
        ArrayList<String> tagColors = intent.getStringArrayListExtra("tagColors");



        if (title != null) {
            exerciseTitle.setText(title);
        }


        if(imagePath != null){
            loadImage(imagePath, exerciseImage);
        }


        if (instructions != null) {
            setInstructions(instructions, exerciseInstructions);
        }

        assert tagColors != null;
        assert tagNames != null;
        if (tagColors.size() != tagNames.size()) {
            throw new RuntimeException();
        }

        for (int i = 0; i < tagNames.size(); i++) {
            Chip chip = new Chip(this);
            chip.setText(tagNames.get(i));

            switch (tagNames.get(i)) {
                case "Upper Body":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundUpperBody)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextUpperBody));
                    break;
                case "Lower Body":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundLowerBody)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextLowerBody));
                    break;
                case "Core":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundCore)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextCore));
                    break;
                case "Cardio":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundCardio)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextCardio));
                    break;
                case "Full Body":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundFullBody)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextFullBody));
                    break;
                case "Beginner":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundBeginner)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextBeginner));
                    break;
                case "Intermediate":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundIntermediate)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextIntermediate));
                    break;
                case "Advanced":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundAdvanced)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextAdvanced));
                    break;
                default:
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.chipBackgroundNoEquipment)));
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chipTextNoEquipment));
                    break;
            }

            tagContainer.addView(chip);
        }


    }


    private void setInstructions(ArrayList<String> instructions, LinearLayout exerciseInstructions) {
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

            TextView instruction = new TextView(exerciseInstructions.getContext());
            instruction.setText(instructionText);
            instruction.setTextSize(18);

            LinearLayout.LayoutParams instructionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            instructionParams.setMargins(16, 8, 16, 8);
            instruction.setLayoutParams(instructionParams);

            instructionLine.addView(num);
            instructionLine.addView(instruction);
            exerciseInstructions.addView(instructionLine);
        }
    }

    private void loadImage(String imagePath, ShapeableImageView imageView) {
        if (imagePath.startsWith("http")) {
            loadImageFromURL(imagePath, imageView);
        } else {
            loadImageFromAssets(imagePath, imageView);
        }
    }

    private void loadImageFromURL(String imageUrl, ShapeableImageView imageView) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                handler.post(() -> imageView.setImageBitmap(bitmap));
                inputStream.close();
            } catch (Exception e) {
                Log.e("loadImageFromURL", "Error: " + e.getMessage());
            }
        });
    }

    private void loadImageFromAssets(String imagePath, ShapeableImageView imageView) {
        try {
            InputStream inputStream = getAssets().open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException e) {
            Log.e("loadImageFromAssets: ", Objects.requireNonNull(e.getMessage()));
        }
    }
}
