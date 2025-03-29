package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
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
import comp3350.gymbuddy.logic.exception.ApplicationInitException;
import comp3350.gymbuddy.logic.exception.DataAccessException;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.util.ErrorHandler;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;
import comp3350.gymbuddy.presentation.util.FileHandler;
import comp3350.gymbuddy.presentation.util.NavigationHelper;
import comp3350.gymbuddy.logic.util.ConfigLoader;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.presentation.util.ToastErrorDisplay;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private WorkoutProfileAdapter workoutProfileAdapter;
    private final List<WorkoutProfile> workoutProfiles = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private final ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(this));
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
            initializeApplication();
        } catch (ApplicationInitException e) {
            handler.handle(e);
        }

        setupUI();
    }

    /**
     * Set up UI components after initialization
     */
    private void setupUI() throws DataAccessException {
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

            WorkoutManager workoutManager = ApplicationService.getInstance().getWorkoutManager();

            // Delete the workout profile
            workoutManager.deleteWorkout(profile.getID());

            // Show success toast
            Toast.makeText(MainActivity.this,
                    R.string.workout_deleted,
                    Toast.LENGTH_SHORT).show();

            // Refresh the list
            loadWorkoutProfiles();

        });

        recyclerViewWorkouts.setAdapter(workoutProfileAdapter);

        // Load workout profiles at the end
        loadWorkoutProfiles();
    }

    /**
     * Loads workout profiles from the database
     */
    private void loadWorkoutProfiles() throws DataAccessException {
        // Safety check for all required elements
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadWorkoutProfiles();
    }

    /**
     * Initializes the application.
     */
    private void initializeApplication() throws ApplicationInitException{
        // Create error display for toast messages
        String dbDirPath = getString(R.string.db_dir);

        // Check if both required files exist
        boolean scriptExists = FileHandler.fileExists(this, dbDirPath, getString(R.string.project_script));
        boolean configExists = FileHandler.fileExists(this, dbDirPath, getString(R.string.dbconfig_properties));
        boolean dbFilesExist = scriptExists && configExists;

        Map<String, String> paths = new HashMap<>();

        if (dbFilesExist) {
            // Files exist, just get their paths
            Timber.tag(TAG).i("Database files already exist");
            paths.put(getString(R.string.script_path_key), FileHandler.getFilePath(this, dbDirPath, getString(R.string.project_script)));
            paths.put(getString(R.string.config_path_key), FileHandler.getFilePath(this, dbDirPath, getString(R.string.dbconfig_properties)));
        } else {
            // Extract files
            Timber.tag(TAG).i("Extracting database files");
            String[] filesToExtract = { getString(R.string.project_script_relative_path), getString(R.string.dbconfig_relative_path) };
            Map<String, String> extractedPaths = FileHandler.extractAssetFiles(this, filesToExtract, dbDirPath,handler);

            if (extractedPaths == null) {
                throw new ApplicationInitException("Failed to extract database files");
            }
            paths.put(getString(R.string.script_path_key), extractedPaths.get(getString(R.string.project_script)));
            paths.put(getString(R.string.config_path_key), extractedPaths.get(getString(R.string.dbconfig_properties)));
        }

        // Create config with dbFilesExist flag
        ConfigLoader config = ConfigLoader.builder()
                .scriptPath(paths.get(getString(R.string.script_path_key)))
                .configPath(paths.get(getString(R.string.config_path_key)))
                .testMode(false)
                .dbAlreadyExists(dbFilesExist)
                .build();

        // Initialize application
        ApplicationService.getInstance().initialize(config);
    }

    @Override
    protected void onDestroy() {
        // Only close ApplicationService if we're finishing the activity completely
        if (isFinishing()) {
            ApplicationService.getInstance().close();
        }

        // Clear references
        binding = null;

        super.onDestroy();
    }
}