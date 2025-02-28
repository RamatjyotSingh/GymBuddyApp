package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutBuilderBinding;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.adapters.WorkoutAdapter;
import comp3350.gymbuddy.presentation.fragments.AddExerciseDialogFragment;
import comp3350.gymbuddy.presentation.util.DSOBundler;
import comp3350.gymbuddy.presentation.util.FormValidator;

public class WorkoutBuilderActivity extends AppCompatActivity {
    private ActivityWorkoutBuilderBinding binding;

    private final ActivityResultLauncher<Intent> exerciseListLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleExerciseListActivityResult);

    private WorkoutAdapter adapter;

    private List<WorkoutItem> workoutItems;

    private Exercise selectedExercise;
    private FormValidator formValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBuilderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.recyclerWorkoutItems.setLayoutManager(new LinearLayoutManager(this));

        getSupportFragmentManager().setFragmentResultListener("workout_item", this, this::handleWorkoutItemResult);

        workoutItems = new ArrayList<>();
        selectedExercise = null;

        adapter = new WorkoutAdapter(workoutItems);
        binding.recyclerWorkoutItems.setAdapter(adapter);

        initializeFormValidator();
    }

    private void initializeFormValidator() {
        formValidator = new FormValidator(this);

        // Set up the constraints for the form.
        formValidator.addEditText(R.id.edtWorkoutName).notEmpty();
    }

    private @Nullable WorkoutProfile createWorkoutProfile() {
        WorkoutProfile result = null;

        if (formValidator.validateAll()) {
            if (workoutItems.size() > 0) {
                String name = formValidator.getString(R.id.edtWorkoutName);
                result = new WorkoutProfile(name, "drawable/ic_default_workout.xml", workoutItems);
            } else {
                Toast.makeText(this, "Workout must have an exercise.", Toast.LENGTH_LONG).show();
            }
        }

        return result;
    }

    public void onClickSave(View v) {
        WorkoutProfile profile = createWorkoutProfile();

        if (profile != null) {
            // Save the profile to the database.
            var accessWorkoutProfiles = new AccessWorkoutProfiles();
            // accessWorkoutProfiles.addProfile(profile); // TODO
        }
    }

    public void onClickFAB(View v) {
        Intent intent = new Intent(this, ExerciseListActivity.class);
        exerciseListLauncher.launch(intent);
    }

    private void handleWorkoutItemResult(String requestKey, Bundle bundle) {
        var dsoBundler = new DSOBundler();
        WorkoutItem workoutItem = dsoBundler.unbundleWorkoutItem(bundle);

        if (workoutItem != null) {
            adapter.addWorkoutItem(workoutItem);
        }
    }

    private void handleExerciseListActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            // Get selected exercise from result.
            int selectedExerciseId = result.getData().getIntExtra("exerciseID",-1);

            if (selectedExerciseId >= 0) {
                var dialog = AddExerciseDialogFragment.newInstance(selectedExerciseId);
                dialog.show(getSupportFragmentManager(), "add_workout_item_dialog");
            }
        }
    }
}
