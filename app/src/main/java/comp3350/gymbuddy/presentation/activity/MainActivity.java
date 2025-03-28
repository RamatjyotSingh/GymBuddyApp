package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;
import comp3350.gymbuddy.presentation.util.NavigationHelper;
import comp3350.gymbuddy.presentation.util.ErrorDisplay;
import comp3350.gymbuddy.presentation.util.FileExtractor;
import comp3350.gymbuddy.logic.util.ConfigLoader;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.exception.ApplicationInitException;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private WorkoutProfileAdapter workoutProfileAdapter;
    private final List<WorkoutProfile> workoutProfiles = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private boolean applicationInitialized = false;
    private boolean uiInitialized = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up Timber for logging
        if (Timber.forest().isEmpty()) {
            Timber.plant(new Timber.DebugTree());
        }
        
        // Set up view binding first
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        try {
            // Show loading state
            binding.loadingOverlay.setVisibility(View.VISIBLE);
            binding.mainContent.setVisibility(View.GONE);
            binding.bottomNavigationView.setVisibility(View.GONE);
    
            // Initialize application directly
            boolean success = initializeApplication();
            applicationInitialized = success;
            
            if (success) {
                // Hide loading overlay and show content
                binding.loadingOverlay.setVisibility(View.GONE);
                binding.mainContent.setVisibility(View.VISIBLE);
                binding.bottomNavigationView.setVisibility(View.VISIBLE);

                // Set up UI after successful initialization
                setupUI();
            } else {
                // Show error and retry option
                Toast.makeText(this, getString(R.string.error_loading_db), Toast.LENGTH_LONG).show();
                binding.loadingOverlay.setOnClickListener(v -> recreate());
            }
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Error in onCreate");
            Toast.makeText(this, "Failed to initialize application", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Set up UI components after initialization
     */
    private void setupUI() {
        try {
            // Initialize navigation helper
            NavigationHelper navigationHelper = new NavigationHelper(this);
    
            // Set up BottomNavigationView using the navigation helper
            navigationHelper.setupBottomNavigation(binding.bottomNavigationView, R.id.home);
    
            // Initialize RecyclerView
            RecyclerView recyclerViewWorkouts = binding.recyclerViewWorkouts;
            recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));
    
            // Initialize adapter with already created list
            workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);
    
            // Set up delete functionality only in this activity
            workoutProfileAdapter.setShowDeleteButtons(true);
            workoutProfileAdapter.setOnProfileDeleteListener((profile, position) -> {
                try {
                    WorkoutManager workoutManager = ApplicationService.getInstance().getWorkoutManager();
                    
                    // Delete the workout profile
                    workoutManager.deleteWorkout(profile.getID());

                    // Show success toast
                    Toast.makeText(MainActivity.this,
                            R.string.workout_deleted,
                            Toast.LENGTH_SHORT).show();

                    // Refresh the list
                    loadWorkoutProfiles();
                } catch (IllegalStateException e) {
                    Toast.makeText(MainActivity.this, 
                            "Application not properly initialized. Please restart the app.", 
                            Toast.LENGTH_LONG).show();
                } catch (DBException e) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.error_deleting_workout) + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

            recyclerViewWorkouts.setAdapter(workoutProfileAdapter);
            
            // Mark UI as initialized
            uiInitialized = true;
    
            // Load workout profiles at the end
            loadWorkoutProfiles();
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Error in setupUI");
        }
    }

    /**
     * Loads workout profiles from the database
     */
    private void loadWorkoutProfiles() {
        // Safety check for all required elements
        if (!applicationInitialized || workoutProfiles == null || 
                workoutProfileAdapter == null || !uiInitialized) {
            Timber.tag(TAG).w("Cannot load profiles: App initialization incomplete");
            return;
        }
        
        try {
            WorkoutManager workoutManager = ApplicationService.getInstance().getWorkoutManager();
            
            // Just clear the existing list
            workoutProfiles.clear();
            
            // Get profiles and add them to the list
            List<WorkoutProfile> profiles = workoutManager.getSavedWorkouts();
            if (profiles != null) {
                workoutProfiles.addAll(profiles);
            }

            // Update the adapter
            if (workoutProfileAdapter != null) {
                workoutProfileAdapter.notifyDataSetChanged();
            }
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Application not properly initialized. Please restart the app.", Toast.LENGTH_LONG).show();
            Timber.tag(TAG).e(e, "ApplicationService not initialized in MainActivity");
        } catch (DBException e) {
            Toast.makeText(this, getString(R.string.error_loading_workout_profiles), Toast.LENGTH_LONG).show();
            Timber.tag(TAG).e(e, "Database error in MainActivity: %s", e.getMessage());
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Unexpected error loading workout profiles: %s", e.getMessage());
            Toast.makeText(this, "Error loading workout profiles", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Only update UI if both application and UI are fully initialized
        if (applicationInitialized && uiInitialized && workoutProfileAdapter != null) {
            try {
                // Clear any highlighted items when returning to the activity
                workoutProfileAdapter.clearHighlight();
                
                // Reload data when returning to this activity
                loadWorkoutProfiles();
            } catch (Exception e) {
                Timber.tag(TAG).e(e, "Error in onResume");
            }
        } else {
            Timber.tag(TAG).d("Skipping onResume updates: initialization incomplete");
        }
    }

    /**
     * Initialize the application
     * @return true if initialization was successful
     */
    private boolean initializeApplication() {
        // Create error display for toast messages
        ErrorDisplay errorDisplay = message -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
     
        
        try {
            // Get database directory path
            String dbDirPath = getString(R.string.db_dir);
            
            // Check if both required files exist
            boolean scriptExists = FileExtractor.fileExists(this, dbDirPath, "Project.script");
            boolean configExists = FileExtractor.fileExists(this, dbDirPath, "DBConfig.properties");
            boolean dbFilesExist = scriptExists && configExists;
            
            Map<String, String> paths = new HashMap<>();
            
            if (dbFilesExist) {
                // Files exist, just get their paths
                Timber.tag(TAG).i("Database files already exist");
                paths.put("scriptPath", FileExtractor.getFilePath(this, dbDirPath, "Project.script"));
                paths.put("configPath", FileExtractor.getFilePath(this, dbDirPath, "DBConfig.properties"));
            } else {
                // Extract files
                Timber.tag(TAG).i("Extracting database files");
                String[] filesToExtract = {"db/Project.script", "db/DBConfig.properties"};
                Map<String, String> extractedPaths = FileExtractor.extractAssetFiles(
                    this, filesToExtract, dbDirPath, errorDisplay);
                    
                if (extractedPaths == null) {
                    errorDisplay.showError(getString(R.string.error_loading_db));
                    Timber.tag(TAG).e("Failed to extract files");
                    return false;
                }
                
                paths.put("scriptPath", extractedPaths.get("Project.script"));
                paths.put("configPath", extractedPaths.get("DBConfig.properties"));
            }
            
            // Create config with dbFilesExist flag
            ConfigLoader config = ConfigLoader.builder()
                    .scriptPath(paths.get("scriptPath"))
                    .configPath(paths.get("configPath"))
                    .testMode(false)
                    .dbAlreadyExists(dbFilesExist)
                    .build();
            
            // Initialize application
            ApplicationService.getInstance().initialize(config);
            return ApplicationService.getInstance().isActive();
        } catch (ApplicationInitException e) {
            errorDisplay.showError(getString(R.string.error_loading_db));
            Timber.tag(TAG).e(e, "Application initialization failed");
            return false;
        } catch (Exception e) {
            errorDisplay.showError(getString(R.string.error_loading_db));
            Timber.tag(TAG).e(e, "Unexpected error during initialization: %s", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        // Only close ApplicationService if we're finishing the activity completely
        if (isFinishing()) {
            try {
                ApplicationService.getInstance().close();
            } catch (Exception e) {
                Timber.tag(TAG).e(e, "Error while closing application service");
            }
        }
        
        // Clear references
        binding = null;
        
        super.onDestroy();
    }
}