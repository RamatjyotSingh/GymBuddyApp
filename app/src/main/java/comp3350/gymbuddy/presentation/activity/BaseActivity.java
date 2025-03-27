package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.gymbuddy.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}