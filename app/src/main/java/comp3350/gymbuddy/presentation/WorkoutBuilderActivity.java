package comp3350.gymbuddy.presentation;

import android.view.View;
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
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;

public class WorkoutBuilderActivity extends AppCompatActivity {
    private WorkoutAdapter adapter;
    private List<WorkoutItem> workoutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_builder);

        RecyclerView recyclerView = findViewById(R.id.recyclerWorkoutItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutItems = new ArrayList<>();
        adapter = new WorkoutAdapter(workoutItems);
        recyclerView.setAdapter(adapter);
    }

    private Dialog createAddWorkoutItemDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_workout_item);

        Button btnSelectExercise = dialog.findViewById(R.id.btnSelectExercise);
        Button btnAddWorkoutItem = dialog.findViewById(R.id.btnAddWorkoutItem);

        btnSelectExercise.setOnClickListener(v -> {
            Toast.makeText(this, "Exercise Selector Not Implemented", Toast.LENGTH_SHORT).show();
        });

        btnAddWorkoutItem.setOnClickListener(v -> onClickAddWorkoutItem(dialog));

        return dialog;
    }

    private WorkoutProfile createWorkoutProfileFromInputFields() {
        // Get all the input views.
        EditText edtWorkoutName = findViewById(R.id.edtWorkoutName);

        var name = edtWorkoutName.getText().toString();
        var items = adapter.getWorkoutItems();
        return new WorkoutProfile(name, "drawable/ic_default_workout.xml", items);
    }

    public void onClickIcon(View v) {
        Toast.makeText(this, "Icon Selector Not Implemented", Toast.LENGTH_SHORT).show();
    }

    public void onClickSave(View v) {
        WorkoutProfile profile = createWorkoutProfileFromInputFields();

        // Save the profile to the database.
        var accessWorkoutProfiles = new AccessWorkoutProfiles();
        accessWorkoutProfiles.addProfile(profile);
    }

    public void onClickFAB(View v) {
        Dialog dialog = createAddWorkoutItemDialog();
        dialog.show();
    }

    private void onClickAddWorkoutItem(Dialog dialog) {
        // This is a placeholder exercise.
        List<Tag> tags = new ArrayList<>(Arrays.asList(new Tag[]{new Tag("Upper Body", "#c9d8fa")}));
        Exercise selectedExercise = new Exercise("Bicep Curl", tags, "Curl the dumbbell.");

        EditText edtSets = dialog.findViewById(R.id.edtSets);
        EditText edtReps = dialog.findViewById(R.id.edtReps);
        EditText edtWeight = dialog.findViewById(R.id.edtWeight);
        EditText edtTime = dialog.findViewById(R.id.edtTime);

        // Parse the values from the data fields.
        int sets = Integer.parseInt(edtSets.getText().toString());
        int reps = Integer.parseInt(edtReps.getText().toString());
        double weight = edtWeight.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(edtWeight.getText().toString());
        double time = edtTime.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(edtTime.getText().toString());

        WorkoutItem newItem = new WorkoutItem(selectedExercise, sets, reps, weight, time);
        adapter.addWorkoutItem(newItem);
        dialog.dismiss();
    }
}
