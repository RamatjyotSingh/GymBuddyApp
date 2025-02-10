package comp3350.gymbuddy.presentation;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.R;

public class WorkoutBuilderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<WorkoutItem> workoutItems;

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

        btnSelectExercise.setOnClickListener(v -> {
            // TODO: Implement Exercise Selector Screen (future feature)
            Toast.makeText(this, "Exercise Selector Not Implemented", Toast.LENGTH_SHORT).show();
        });

        btnAddWorkoutItem.setOnClickListener(v -> {
            // This is a placeholder exercise.
            List<Tag> tags = new ArrayList<Tag>(Arrays.asList(new Tag[]{new Tag("Upper Body", "#c9d8fa")}));
            Exercise selectedExercise = new Exercise("Bicep Curl", tags, "Curl the dumbbell.");

            // Parse the values from the data fields.
            int sets = Integer.parseInt(edtSets.getText().toString());
            int reps = Integer.parseInt(edtReps.getText().toString());
            double weight = edtWeight.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(edtWeight.getText().toString());
            double time = edtTime.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(edtTime.getText().toString());

            WorkoutItem newItem = new WorkoutItem(selectedExercise, sets, reps, weight, time);
            adapter.addWorkoutItem(newItem);
            dialog.dismiss();
        });

        dialog.show();
    }
}
