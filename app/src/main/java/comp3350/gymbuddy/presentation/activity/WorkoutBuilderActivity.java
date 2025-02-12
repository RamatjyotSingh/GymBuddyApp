package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.R;
import comp3350.gymbuddy.presentation.adapters.WorkoutAdapter;

public class WorkoutBuilderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<WorkoutItem> workoutItems;
    private Exercise selectedExercise; // Store selected exercise
    private Button btnSelectExercise; // Reference to update UI in the dialog

    // Register Activity Result for selecting an exercise
    private final ActivityResultLauncher<Intent> selectExerciseLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_builder);

        recyclerView = findViewById(R.id.recyclerWorkoutItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutItems = new ArrayList<>();
        adapter = new WorkoutAdapter(workoutItems);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAddItem).setOnClickListener(v -> showAddWorkoutItemDialog());
    }

    private void showAddWorkoutItemDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_workout_item);

        Button btnSelectExercise = dialog.findViewById(R.id.btnSelectExercise);
        EditText edtSets = dialog.findViewById(R.id.edtSets);
        EditText edtReps = dialog.findViewById(R.id.edtReps);
        EditText edtWeight = dialog.findViewById(R.id.edtWeight);
        EditText edtTime = dialog.findViewById(R.id.edtTime);
        Button btnAddWorkoutItem = dialog.findViewById(R.id.btnAddWorkoutItem);

        // Reset selected exercise & UI
        selectedExercise = null;
        btnSelectExercise.setText("Select Exercise");

        // Open ExerciseListActivity to select an exercise
        btnSelectExercise.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseListActivity.class);
            selectExerciseLauncher.launch(intent);
        });

        // Add Workout Item button
        btnAddWorkoutItem.setOnClickListener(v -> {
            // ✅ Check if an exercise is selected
            if (selectedExercise == null) {
                btnSelectExercise.setError("Please select an exercise.");
                return;
            } else {
                btnSelectExercise.setError(null);
            }

            // ✅ Validate numeric inputs (sets & reps must not be empty)
            if (edtSets.getText().toString().trim().isEmpty()) {
                edtSets.setError("Required");
                return;
            }
            if (edtReps.getText().toString().trim().isEmpty()) {
                edtReps.setError("Required");
                return;
            }

            // ✅ Parse values safely
            int sets = Integer.parseInt(edtSets.getText().toString().trim());
            int reps = Integer.parseInt(edtReps.getText().toString().trim());
            double weight = edtWeight.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(edtWeight.getText().toString().trim());
            double time = edtTime.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(edtTime.getText().toString().trim());

            // ✅ Create and add the workout item
            WorkoutItem newItem = new WorkoutItem(selectedExercise, sets, reps, weight, time);
            adapter.addWorkoutItem(newItem);

            // ✅ Reset UI
            selectedExercise = null;
            btnSelectExercise.setText("Select Exercise");
            edtSets.setText("");
            edtReps.setText("");
            edtWeight.setText("");
            edtTime.setText("");

            dialog.dismiss();
        });

        dialog.show();
    }

    private void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Intent data = result.getData();

            // Get exercise details from the Intent
            String exerciseName = data.getStringExtra("exerciseName");
            String exerciseInstructions = data.getStringExtra("exerciseInstructions");
            String exerciseImage = data.getStringExtra("exerciseImage");
            ArrayList<String> tagNames = data.getStringArrayListExtra("tagNames");
            ArrayList<String> tagColors = data.getStringArrayListExtra("tagColors");

            // Convert tags from received data
            List<Tag> tags = new ArrayList<>();
            for (int i = 0; i < Objects.requireNonNull(tagNames).size(); i++) {
                assert tagColors != null;
                tags.add(new Tag(tagNames.get(i), tagColors.get(i)));
            }

            // Create Exercise object
            selectedExercise = new Exercise(exerciseName, tags, exerciseInstructions, exerciseImage);

            // Update button text in the dialog
            if (btnSelectExercise != null) {
                btnSelectExercise.setText(exerciseName); // Show selected exercise name
            }
        }
    }
}
