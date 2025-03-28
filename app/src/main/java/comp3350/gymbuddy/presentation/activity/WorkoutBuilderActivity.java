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
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.exception.BusinessException;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.presentation.util.ErrorHandler;
import comp3350.gymbuddy.logic.util.InputValidator;
import comp3350.gymbuddy.logic.exception.InvalidInputException;
import comp3350.gymbuddy.logic.exception.InvalidNameException;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.adapters.WorkoutItemAdapter;
import comp3350.gymbuddy.presentation.fragments.AddExerciseDialogFragment;
import comp3350.gymbuddy.presentation.util.DSOBundler;
import comp3350.gymbuddy.presentation.util.NavigationHelper;
import comp3350.gymbuddy.presentation.util.ToastErrorDisplay;

public class WorkoutBuilderActivity extends AppCompatActivity {

    private final ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(this));
    // View binding for accessing UI elements efficiently
    private ActivityWorkoutBuilderBinding binding;

    // Launcher for starting the ExerciseListActivity and handling its result
    private final ActivityResultLauncher<Intent> exerciseListLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleExerciseListActivityResult);

    private WorkoutItemAdapter adapter;

    private List<WorkoutItem> workoutItems;
    private String selectedIconPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize navigation helper
        // Navigation helper component
        NavigationHelper navigationHelper = new NavigationHelper(this);
        
        // Inflate the layout using view binding
        binding = ActivityWorkoutBuilderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the RecyclerView with a LinearLayoutManager for displaying workout items
        binding.recyclerWorkoutItems.setLayoutManager(new LinearLayoutManager(this));

        // Listen for results from the AddExerciseDialogFragment (workout item details)
        getSupportFragmentManager().setFragmentResultListener(getString(R.string.frag_listner_workout_item), this, this::handleWorkoutItemResult);

        // Initialize the workout items list
        workoutItems = new ArrayList<>();

        // Initialize icon with default path
        selectedIconPath = getString(R.string.default_workout_icon_path);

        // Set up the adapter for the RecyclerView
        adapter = new WorkoutItemAdapter(workoutItems);
        adapter.setItemDeleteListener(position -> {
            // Remove the item
            adapter.removeItem(position);
            // Provide feedback
            Toast.makeText(WorkoutBuilderActivity.this, 
                          getString(R.string.exercise_removed), 
                          Toast.LENGTH_SHORT).show();
        });
        // Enable delete buttons for this activity only
        adapter.setShowDeleteButtons(true);
        binding.recyclerWorkoutItems.setAdapter(adapter);

        // Set up bottom navigation using our navigation helper
        navigationHelper.setupBottomNavigation(binding.bottomNavigationView, R.id.build_workouts);
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
     * If the workout profile is valid, it saves the profile and its items to the
     * database
     * and navigates to MainActivity with the created profile.
     */
    public void onClickSave(View v) {
        WorkoutProfile profile = generateWorkoutProfile();

        if (profile != null) {
            try {
                // Get the workout manager from ApplicationService
                WorkoutManager workoutManager = ApplicationService.getInstance().getWorkoutManager();
                workoutManager.saveWorkout(profile);


                    // Show success message
               Toast.makeText(this, "Workout profile saved successfully", Toast.LENGTH_SHORT).show();

                    // Explicitly navigate to MainActivity
                    Intent intent = new Intent(this, MainActivity.class);

                    // Use consistent flag with your navigation approach
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    // Add smooth transition with no animation
                    android.app.ActivityOptions options = android.app.ActivityOptions.makeCustomAnimation(this, 0, 0);
                    startActivity(intent, options.toBundle());



            }
            catch (BusinessException e) {
                handler.handle(e, getString(R.string.error_saving_workout));
            }
            finally {
                finish();
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
     * @param bundle     The data bundle containing workout item details.
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
            int selectedExerciseId = result.getData().getIntExtra(getString(R.string.intent_exerciseid), -1);

            if (selectedExerciseId >= 0) {
                // Open the dialog to configure the selected exercise
                var dialog = AddExerciseDialogFragment.newInstance(selectedExerciseId);
                dialog.show(getSupportFragmentManager(), "add_workout_item_dialog");
            }
        }
    }

}
