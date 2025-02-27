package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import comp3350.gymbuddy.application.DatabaseManager;
import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.R;
import comp3350.gymbuddy.presentation.adapters.WorkoutAdapter;

public class WorkoutBuilderActivity extends AppCompatActivity {
    private WorkoutAdapter adapter;
    private Exercise selectedExercise; // Store selected exercise
    private Button btnSelectExercise; // Reference to update UI in the dialog

    // Register Activity Result for selecting an exercise
    private final ActivityResultLauncher<Intent> selectExerciseLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabase();
        setContentView(R.layout.activity_workout_builder);

        RecyclerView recyclerView = findViewById(R.id.recyclerWorkoutItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<WorkoutItem> workoutItems = new ArrayList<>();
        adapter = new WorkoutAdapter(workoutItems);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAddItem).setOnClickListener(v -> showAddWorkoutItemDialog());
    }

    private void showAddWorkoutItemDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_workout_item);

        btnSelectExercise = dialog.findViewById(R.id.btnSelectExercise);
        EditText edtSets = dialog.findViewById(R.id.edtSets);
        EditText edtReps = dialog.findViewById(R.id.edtReps);
        EditText edtWeight = dialog.findViewById(R.id.edtWeight);
        EditText edtTime = dialog.findViewById(R.id.edtTime);
        Button btnAddWorkoutItem = dialog.findViewById(R.id.btnAddWorkoutItem);

        // Reset selected exercise & UI
        selectedExercise = null;
        btnSelectExercise.setText(R.string.select_exercise);

        // Open ExerciseListActivity to select an exercise
        btnSelectExercise.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseListActivity.class);
            selectExerciseLauncher.launch(intent);
        });

        // Add Workout Item button
        btnAddWorkoutItem.setOnClickListener(v -> {

            if (selectedExercise == null) {
                btnSelectExercise.setError("Please select an exercise.");
                return;
            } else {
                btnSelectExercise.setError(null);
            }

            if (edtSets.getText().toString().trim().isEmpty()) {
                edtSets.setError("Required");
                return;
            }
            if (edtReps.getText().toString().trim().isEmpty()) {
                edtReps.setError("Required");
                return;
            }

            int sets = Integer.parseInt(edtSets.getText().toString().trim());
            int reps = Integer.parseInt(edtReps.getText().toString().trim());
            double weight = edtWeight.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(edtWeight.getText().toString().trim());
            double time = edtTime.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(edtTime.getText().toString().trim());

            WorkoutItem newItem;
            if (time > 0.0) {
                newItem = new TimeBasedWorkoutItem(selectedExercise, sets, time);
            } else {
                newItem = new RepBasedWorkoutItem(selectedExercise, sets, reps, weight);
            }
            adapter.addWorkoutItem(newItem);

            selectedExercise = null;
            btnSelectExercise.setText(R.string.select_exercise);
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
            Intent data = result.getData(); // THIS INTENT SHOULD JUST CONTAIN THE EXERCISE ID

            AccessExercises accessExercises = new AccessExercises();

            // Assign the object we picked from exercise list
            selectedExercise = accessExercises.getExerciseByID(data.getIntExtra("exerciseID", 0));

            // Update button text in the dialog
            if (btnSelectExercise != null) {
                btnSelectExercise.setText(selectedExercise.getName()); // Show selected exercise name
                btnSelectExercise.setError(null);
            }
        }
    }

    //this method helps to initialize the db
    private void initializeDatabase() {
        Context context = getApplicationContext();
        DatabaseManager.init(context);
    }

}
