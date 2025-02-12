package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

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
        ImageView exerciseImage = findViewById(R.id.exerciseImage);
        ChipGroup tagContainer = findViewById(R.id.tagContainer);
        LinearLayout exerciseInstructions = findViewById(R.id.exerciseInstructions);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Get Intent Data
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imagePath = intent.getStringExtra("imagePath");
        String instructions = intent.getStringExtra("instructions");
        ArrayList<String> tagNames = intent.getStringArrayListExtra("tagNames");
        ArrayList<String> tagColors = intent.getStringArrayListExtra("tagColors");





        // Set Title
        if (title != null) {
            exerciseTitle.setText(title);
        }

        // Load Image
        loadImageFromAssets(imagePath, exerciseImage);

        // Set Instructions
        if (instructions != null) {
            setInstructions(instructions, exerciseInstructions);
        }

        if (tagColors.size() != tagNames.size()) {
            throw new RuntimeException();
        }
        // Populate tagNames
        if (tagNames != null && tagColors != null) {
            for (int i = 0; i < tagNames.size(); i++) {
                Chip chip = new Chip(this);
                chip.setText(tagNames.get(i));

                // Set background color using color resources from colors.xml
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


    }

    private void setInstructions(String instructions, LinearLayout exerciseInstructions) {
        String[] lines = instructions.trim().split("\r\n");
        for (String line : lines) {
            LinearLayout instructionLine = new LinearLayout(this);
            instructionLine.setOrientation(LinearLayout.HORIZONTAL);

            // Extract instruction number and text
            String instructionNo = line.substring(0, line.indexOf("."));
            TextView num = new TextView(instructionLine.getContext());
            num.setText(instructionNo);
            num.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_background));


            String instructionText = line.substring(line.indexOf(".") + 1).trim();
            TextView instruction = new TextView(instructionLine.getContext());
            instruction.setText(instructionText);

            // Set margin for 'instruction' TextView
            LinearLayout.LayoutParams instructionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            instructionParams.setMargins(16, 8, 16, 8);  // (left, top, right, bottom)
            instruction.setLayoutParams(instructionParams);

            // Set text size for instruction
            instruction.setTextSize(18);

            // Add both views to the instruction line
            instructionLine.addView(num);
            instructionLine.addView(instruction);

            // Add instruction line to the exercise instructions layout
            exerciseInstructions.addView(instructionLine);
        }
    }

    private void loadImageFromAssets(String imagePath, ImageView imageView) {
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
