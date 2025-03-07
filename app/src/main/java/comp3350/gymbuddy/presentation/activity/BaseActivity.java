package comp3350.gymbuddy.presentation.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    private static final String TAG = "BaseActivity"; // Define proper class tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
    }

    protected void setupBottomNavigation(BottomNavigationView navigationView) {
        this.bottomNavigationView = navigationView;

        // Set correct item without triggering the listener
        updateSelectedItem(navigationView);

        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Don't create a new intent for the current activity
            if ((id == R.id.home && this instanceof MainActivity) ||
                    (id == R.id.build_workouts && this instanceof WorkoutBuilderActivity) ||
                    (id == R.id.workout_log && this instanceof WorkoutLogActivity)) {
                return true;
            }

            // Navigate to selected activity
            navigateToActivity(id);
            return true;
        });
    }

    void initDB() {
        // First ensure directory exists
        File dbDir = new File(getFilesDir(), "db");
        if (!dbDir.exists()) {
            boolean dirCreated = dbDir.mkdirs();
            if (!dirCreated) {
                Log.e(TAG, "Failed to create database directory");
                Toast.makeText(this, "Failed to create database directory", Toast.LENGTH_LONG).show();
                return; // Exit early since we can't proceed without a directory
            }
        }

        // Copy script file if needed
        String destinationPath = getFilesDir().getPath() + "/db/Project.script";
        File destinationFile = new File(destinationPath);

        if (!destinationFile.exists()) {
            // Using try-with-resources for automatic resource closing
            try (InputStream inputStream = getAssets().open("db/Project.script");
                    OutputStream outputStream = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                Log.i(TAG, "Database script copied successfully");
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy database script", e);
                Toast.makeText(this, "Failed to copy database script: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                return; // Exit early since we need the script
            }
        }

        // Initialize database connection using try-with-resources
        // Note: We're not closing HSQLDBHelper here because it's a singleton
        // that needs to remain open for the app's lifecycle.
        try {
            // Since HSQLDBHelper is a singleton and implements AutoCloseable,
            // we just need to call getInstance() and connect(), but don't need
            // to include it in the try-with-resources block
            HSQLDBHelper.getInstance(this).connect();
            Log.i(TAG, "Database initialized successfully");
        } catch (DBException e) {
            Log.e(TAG, "Database initialization failed", e);
            Toast.makeText(this, "Database initialization failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean up database when activity is destroyed
        HSQLDBHelper.resetInstance();
    }

    private void updateSelectedItem(BottomNavigationView navigationView) {
        // Manually set the selected item without triggering listener
        navigationView.setOnItemSelectedListener(null);

        if (this instanceof MainActivity) {
            navigationView.setSelectedItemId(R.id.home);
        } else if (this instanceof WorkoutBuilderActivity) {
            navigationView.setSelectedItemId(R.id.build_workouts);
        } else if (this instanceof WorkoutLogActivity) {
            navigationView.setSelectedItemId(R.id.workout_log);
        }

        // Restore navigation listener after selection
        setupNavigationListener(navigationView);
    }

    private void setupNavigationListener(BottomNavigationView navigationView) {
        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if ((id == R.id.home && this instanceof MainActivity) ||
                    (id == R.id.build_workouts && this instanceof WorkoutBuilderActivity) ||
                    (id == R.id.workout_log && this instanceof WorkoutLogActivity)) {
                return true;
            }

            navigateToActivity(id);
            return true;
        });
    }

    private void navigateToActivity(int menuItemId) {
        Class<?> targetActivityClass = null;

        if (menuItemId == R.id.home) {
            targetActivityClass = MainActivity.class;
        } else if (menuItemId == R.id.build_workouts) {
            targetActivityClass = WorkoutBuilderActivity.class;
        } else if (menuItemId == R.id.workout_log) {
            targetActivityClass = WorkoutLogActivity.class;
        }

        if (targetActivityClass != null && !targetActivityClass.isInstance(this)) {
            Intent intent = new Intent(this, targetActivityClass);
            // Use single top pattern - won't create multiple instances
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

            // Prevent activity accumulation
            if (!(this instanceof MainActivity)) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure correct selection when returning to the activity
        if (bottomNavigationView != null) {
            updateSelectedItem(bottomNavigationView);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }
}