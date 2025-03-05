package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutBuilderBinding;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.logic.InputValidator;
import comp3350.gymbuddy.logic.exception.InvalidInputException;
import comp3350.gymbuddy.logic.exception.InvalidNameException;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.adapters.WorkoutAdapter;
import comp3350.gymbuddy.presentation.fragments.AddExerciseDialogFragment;
import comp3350.gymbuddy.presentation.utils.DSOBundler;

public class WorkoutBuilderActivity extends BaseActivity {

    // View binding for accessing UI elements efficiently
    private ActivityWorkoutBuilderBinding binding;

    // Launcher for starting the ExerciseListActivity and handling its result
    private final ActivityResultLauncher<Intent> exerciseListLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleExerciseListActivityResult);

    private WorkoutAdapter adapter;

    private List<WorkoutItem> workoutItems;
    private String selectedIconPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using view binding
        binding = ActivityWorkoutBuilderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the RecyclerView with a LinearLayoutManager for displaying workout items
        binding.recyclerWorkoutItems.setLayoutManager(new LinearLayoutManager(this));

        // Listen for results from the AddExerciseDialogFragment (workout item details)
        getSupportFragmentManager().setFragmentResultListener("workout_item", this, this::handleWorkoutItemResult);

        // Initialize the workout items list
        workoutItems = new ArrayList<>();

        // Initialize icon with default path
        selectedIconPath = getString(R.string.default_workout_icon_path);

        // Set up the adapter for the RecyclerView
        adapter = new WorkoutAdapter(workoutItems);
        binding.recyclerWorkoutItems.setAdapter(adapter);

        setupBottomNavigation(binding.bottomNavigationView);
    }

    /**
     * Creates a WorkoutProfile instance if all validation rules pass.
     *
     * @return A valid WorkoutProfile instance or null if validation fails.
     */
    private @Nullable WorkoutProfile generateWorkoutProfile() {
        WorkoutProfile workoutProfile = null;

        // Ensure all input is valid before proceeding
        try {
            String name = binding.edtWorkoutName.getText().toString();

            var inputValidator = new InputValidator();
            workoutProfile = inputValidator.newWorkoutProfile(name, selectedIconPath, workoutItems);

            // Report invalid input.
        } catch (InvalidNameException e) {
            binding.edtWorkoutName.setError(e.getMessage());
        } catch (InvalidInputException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return workoutProfile;
    }

 /**
  * Handles the save button click event.
  * If the workout profile is valid, it saves the profile and its items to the database
  * and navigates to the WorkoutLogActivity.
  */
 public void onClickSave(View v) {
     WorkoutProfile profile = generateWorkoutProfile();

     if (profile != null) {

         try {
             // Save the profile to the database
             AccessWorkoutProfiles accessWorkoutProfiles = new AccessWorkoutProfiles();
             accessWorkoutProfiles.insertWorkoutProfile(profile);

             // Show success message
             Toast.makeText(this, "Workout profile saved successfully", Toast.LENGTH_SHORT).show();

             // Close this activity
             finish();
         } catch (Exception e) {
             Log.e("WorkoutBuilder", "onClickSave: " + e.getMessage());
             throw new RuntimeException(e);
         }
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


}
