package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    private WorkoutProfileAdapter workoutProfileAdapter;
    private List<WorkoutProfile> workoutProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        initializeDatabase();

        // Initialize View Binding
        comp3350.gymbuddy.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up BottomNavigationView using the base class
        setupBottomNavigation(binding.bottomNavigationView, R.id.home);

        // Initialize RecyclerView
        RecyclerView recyclerViewWorkouts = binding.recyclerViewWorkouts;
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data structures
        workoutProfiles = new ArrayList<>();
        workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);

        // Set up delete functionality only in this activity
        workoutProfileAdapter.setShowDeleteButtons(true);
        workoutProfileAdapter.setOnProfileDeleteListener((profile, position) -> {
            // Delete the workout profile
            try {
                WorkoutManager workoutManager = new WorkoutManager(true);
                boolean success = workoutManager.deleteWorkout(profile.getID());

                if (success) {
                    // Show success toast
                    Toast.makeText(MainActivity.this,
                            R.string.workout_deleted,
                            Toast.LENGTH_SHORT).show();

                    // Refresh the list
                    loadWorkoutProfiles();
                } else {
                    Toast.makeText(MainActivity.this,
                            R.string.failed_to_delete_workout,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (DBException e) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.error_deleting_workout) + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerViewWorkouts.setAdapter(workoutProfileAdapter);

        // Load workout profiles
        loadWorkoutProfiles();
    }


    /**
     * Loads workout profiles from the database
     */
    private void loadWorkoutProfiles() {
        try {
            WorkoutManager workoutManager = new WorkoutManager(true);
            workoutProfiles.clear();
            workoutProfiles.addAll(workoutManager.getAll());

            workoutProfileAdapter.notifyDataSetChanged();
        } catch (DBException e) {
            Toast.makeText(this, getString(R.string.error_loading_workout_profiles) + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        
        // Clear any highlighted items when returning to the activity
        if (workoutProfileAdapter != null) {
            workoutProfileAdapter.clearHighlight();
        }
        
        // Reload data when returning to this activity
        loadWorkoutProfiles();
    }

    private void initializeDatabase() {
        // Create DB directory
        File dbDir = new File(getFilesDir(), getString(R.string.db_dir));
        if (!dbDir.exists() && !dbDir.mkdirs()) {
            Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            Timber.tag(getString(R.string.tag_mainactivity)).e("Failed to create DB directory");
            return;
        }


        File dbFile = new File(dbDir, getString(R.string.db_script));

        if(dbFile.exists() && dbFile.length() > 0) {
            Timber.tag(getString(R.string.tag_mainactivity)).d("Database already exists");
            HSQLDBHelper.setDatabaseDirectoryPath(dbDir.getAbsolutePath());
            return;
        }

        Timber.tag(getString(R.string.tag_mainactivity)).d("Database needs to be initialized");

        // Extract required files
        String[] dbFiles = {"db/Project.script", "db/DBConfig.properties"};
        for (String filepath : dbFiles) {
            String filename = new File(filepath).getName();
            extractFile(filepath, dbDir, filename);
        }

        // Set path and initialize database
        HSQLDBHelper.setDatabaseDirectoryPath(dbDir.getAbsolutePath());
        try {
            HSQLDBHelper.init();
            Timber.tag(getString(R.string.tag_mainactivity)).d("Database initialized successfully");
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_loading_db), Toast.LENGTH_LONG).show();
            Timber.tag(getString(R.string.tag_mainactivity)).e("Failed to initialize database: %s", e.getMessage());
        }
    }

    /**
     * Extract a specific file from assets to the destination directory
     *
     * @param assetPath Source path in assets
     * @param destDir Destination directory
     * @param destFilename Destination filename
     */
    private void extractFile(String assetPath, File destDir, String destFilename) {
        File outputFile = new File(destDir, destFilename);
        timber.log.Timber.tag("Main").d("Extracting file: %s → %s", assetPath, outputFile.getAbsolutePath());

        // Skip if file already exists and is not empty
        if (outputFile.exists() && outputFile.length() > 0) {
            Timber.tag(getString(R.string.tag_mainactivity)).d("File already exists: %s", destFilename);
            return;
        }

        try (InputStream is = getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8192]; // 8KB buffer for better performance
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            Timber.tag(getString(R.string.tag_mainactivity)).d("Extracted file: %s → %s", assetPath, outputFile.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            Timber.tag(getString(R.string.tag_mainactivity)).e("Failed to extract file: %s - %s", assetPath, e.getMessage());
        }
    }
}