package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Context;
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
import comp3350.gymbuddy.application.DatabaseManager;

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
    // View binding for accessing UI elements efficiently
    private ActivityWorkoutBuilderBinding binding;


    // Launcher for starting the ExerciseListActivity and handling its result
    private final ActivityResultLauncher<Intent> exerciseListLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleExerciseListActivityResult);

    private WorkoutAdapter adapter;

    private List<WorkoutItem> workoutItems;

    // The exercise selected from the exercise list.
    private Exercise selectedExercise;

    // Form validator to ensure workout name is provided
    private FormValidator formValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabase();
        // Inflate the layout using view binding
        binding = ActivityWorkoutBuilderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Set up the RecyclerView with a LinearLayoutManager for displaying workout items
        binding.recyclerWorkoutItems.setLayoutManager(new LinearLayoutManager(this));

        // Listen for results from the AddExerciseDialogFragment (workout item details)
        getSupportFragmentManager().setFragmentResultListener("workout_item", this, this::handleWorkoutItemResult);

        // Initialize the workout items list
        workoutItems = new ArrayList<>();
        selectedExercise = null;

        // Set up the adapter for the RecyclerView
        adapter = new WorkoutAdapter(workoutItems);
        binding.recyclerWorkoutItems.setAdapter(adapter);

        // Initialize form validation logic
        initializeFormValidator();
    }

    /**
     * Initializes form validation rules.
     * Ensures the workout name is not empty before saving.
     */
    private void initializeFormValidator() {
        formValidator = new FormValidator(this);
        formValidator.addEditText(R.id.edtWorkoutName).notEmpty();
    }

    /**
     * Creates a WorkoutProfile instance if all validation rules pass.
     * Ensures that at least one exercise is included before proceeding.
     *
     * @return A valid WorkoutProfile instance or null if validation fails.
     */
    private @Nullable WorkoutProfile createWorkoutProfile() {
        WorkoutProfile result = null;

        if (formValidator.validateAll()) {
            if (workoutItems.size() > 0) {
                // Retrieve the validated workout name
                String name = formValidator.getString(R.id.edtWorkoutName);
                // Create a workout profile with a default icon
                result = new WorkoutProfile(name, "drawable/ic_default_workout.xml", workoutItems);
            } else {
                // Notify the user that at least one exercise is required
                Toast.makeText(this, "Workout must have an exercise.", Toast.LENGTH_LONG).show();
            }
        }

        return result;
    }

    /**
     * Handles the save button click event.
     * If the workout profile is valid, it saves the profile to the database.
     */
    public void onClickSave(View v) {
        WorkoutProfile profile = createWorkoutProfile();

        if (profile != null) {
            // Save the profile to the database.
            var accessWorkoutProfiles = new AccessWorkoutProfiles();
            // accessWorkoutProfiles.addProfile(profile); // TODO: Implement database saving logic
        }
    }

    /**
     * Handles the Floating Action Button (FAB) click event.
     * Opens the ExerciseListActivity for the user to select an exercise.
     */
    public void onClickFAB(View v) {
        Intent intent = new Intent(this, ExerciseListActivity.class);
        exerciseListLauncher.launch(intent);
    }

    /**
     * Handles the result from the AddExerciseDialogFragment.
     * Extracts the workout item from the bundle and adds it to the adapter.
     *
     * @param requestKey The key identifying the request.
     * @param bundle The data bundle containing workout item details.
     */
    private void handleWorkoutItemResult(String requestKey, Bundle bundle) {
        var dsoBundler = new DSOBundler();
        WorkoutItem workoutItem = dsoBundler.unbundleWorkoutItem(bundle);

        if (workoutItem != null) {
            // Add the new workout item to the adapter
            adapter.addWorkoutItem(workoutItem);
        }
    }

    /**
     * Handles the result from ExerciseListActivity.
     * If an exercise was selected, it opens the AddExerciseDialogFragment.
     *
     * @param result The result data from ExerciseListActivity.
     */
    private void handleExerciseListActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            // Get the selected exercise ID from the result data
            int selectedExerciseId = result.getData().getIntExtra("exerciseID", -1);

            if (selectedExerciseId >= 0) {
                // Open the dialog to configure the selected exercise
                var dialog = AddExerciseDialogFragment.newInstance(selectedExerciseId);
                dialog.show(getSupportFragmentManager(), "add_workout_item_dialog");
            }
        }
    }

    //this method helps to initialize the db
    private void initializeDatabase() {
        Context context = getApplicationContext();
        DatabaseManager.init(context);
    }

}
