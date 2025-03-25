package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.gymbuddy.R;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the selected item in the nav bar with callback functions for each item.
     * @param navigationView The navigation bar to be set up.
     * @param currentNavBarItemId The ID of the nav bar item corresponding to the calling activity.
     */
    protected void setupBottomNavigation(BottomNavigationView navigationView, int currentNavBarItemId){
        // Set selected item in nav bar
        navigationView.setSelectedItemId(currentNavBarItemId);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem;

        // Set on-click listeners for menu items
        // Does not set an on-click listener for the menu item corresponding to the current activity
        if(currentNavBarItemId != R.id.home){
            menuItem = menu.findItem(R.id.home);
            menuItem.setOnMenuItemClickListener(item -> {
               navigateToActivity(MainActivity.class);
               return true;
            });
        }

        if(currentNavBarItemId != R.id.build_workouts){
            menuItem = menu.findItem(R.id.build_workouts);
            menuItem.setOnMenuItemClickListener(item -> {
               navigateToActivity(WorkoutBuilderActivity.class);
               return true;
            });
        }

        if(currentNavBarItemId != R.id.workout_log){
            menuItem = menu.findItem(R.id.workout_log);
            menuItem.setOnMenuItemClickListener(item -> {
               navigateToActivity(WorkoutLogActivity.class);
               return true;
            });
        }
    }

    /**
     * Create an intent with the passed activity, and start the activity with the created intent.
     * @param activity The activity to be started.
     */
    private void navigateToActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);

        // Use single top pattern - won't create multiple instances
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}