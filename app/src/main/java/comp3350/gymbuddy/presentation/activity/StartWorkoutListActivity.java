package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityStartWorkoutListBinding;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.WorkoutItemAdapter;
import timber.log.Timber;

/**
 * Activity for displaying workout profile details and starting a workout session
 */
public class StartWorkoutListActivity extends AppCompatActivity {

    private static final String TAG = "StartWorkoutListActivity";
    private static final String ERROR_MESSAGE_PROFILE = "Unable to retrieve workout profile, please try again later.";
    private static final String ERROR_MESSAGE_ITEMS = "Unable to retrieve workout items, please try again later.";
    private static final String EXTRA_WORKOUT_PROFILE_ID = "workoutProfileId";
    
    private ActivityStartWorkoutListBinding binding;
    private WorkoutManager workoutManager;
    private int profileId = -1;
    private WorkoutProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up view binding
        binding = ActivityStartWorkoutListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Configure toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize managers
        workoutManager = new WorkoutManager(true);

        // Show loading state
        showLoadingState();
        
        // Get profile ID from intent
        profileId = getIntent().getIntExtra(EXTRA_WORKOUT_PROFILE_ID, -1);
        
        // Validate profile ID
        if (profileId == -1) {
            handleError(ERROR_MESSAGE_PROFILE);
            return;
        }
        
        // Load workout profile
        loadProfile();
        
        // Set up start workout button
        binding.buttonStartWorkout.setOnClickListener(v -> startWorkout());
    }
    
    /**
     * Load the workout profile from the database
     */
    private void loadProfile() {
        try {
            profile = workoutManager.getWorkoutProfileByID(profileId);
            
            if (profile != null) {
                displayProfile();
            } else {
                handleError("Profile not found with ID: " + profileId);
            }
        } catch (DBException e) {
            Timber.tag(TAG).e(e, "Error loading profile with ID: %d", profileId);
            handleError(ERROR_MESSAGE_ITEMS);
        }
    }
    
    /**
     * Display the workout profile
     */
    private void displayProfile() {
        // Set workout name
        binding.workoutName.setText(profile.getName());
        
        // Set up the RecyclerView
        binding.exerciseRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        
        // Get workout items from profile
        List<WorkoutItem> workoutItems = profile.getWorkoutItems();
        
        // Create and set adapter
        WorkoutItemAdapter adapter = new WorkoutItemAdapter(workoutItems);
        binding.exerciseRecyclerView.setAdapter(adapter);
        
        // Update UI based on content
        updateUIState();
        
        // Show content
        showContentState();
        
        // Log success
        Timber.tag(TAG).d("Displayed workout profile '%s' with %d exercises", 
                profile.getName(), workoutItems.size());
    }
    
    /**
     * Update UI elements based on profile content
     */
    private void updateUIState() {
        List<WorkoutItem> workoutItems = profile.getWorkoutItems();
        
        if (workoutItems.isEmpty()) {
            // Should never happen based on your comment, but good practice to handle
            binding.emptyStateView.setVisibility(View.VISIBLE);
            binding.exerciseRecyclerView.setVisibility(View.GONE);
            binding.buttonStartWorkout.setEnabled(false);
        } else {
            binding.emptyStateView.setVisibility(View.GONE);
            binding.exerciseRecyclerView.setVisibility(View.VISIBLE);
            binding.buttonStartWorkout.setEnabled(true);
        }
        
        // Show exercise count
        binding.textExerciseCount.setText(getString(R.string.exercise_count_format, workoutItems.size()));
    }
    
    /**
     * Start a workout session with this profile
     */
    private void startWorkout() {
        Intent intent = new Intent(this, WorkoutPlayerActivity.class);
        intent.putExtra("workoutProfileId", profileId);
        startActivity(intent);
    }
    
    /**
     * Show loading state
     */
    private void showLoadingState() {
        binding.viewSwitcher.setDisplayedChild(0);
    }
    
    /**
     * Show content state
     */
    private void showContentState() {
        binding.viewSwitcher.setDisplayedChild(1);
    }
    
    /**
     * Handle errors by showing a message and finishing the activity
     */
    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Timber.tag(TAG).e(message);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Avoid memory leaks
    }
}
