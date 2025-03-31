package comp3350.gymbuddy.presentation.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

/**
 * Helper class for navigation operations across the app.
 * Provides functionality for bottom navigation setup and activity transitions.
 */
public class NavigationHelper {
    private final Activity activity;

    public NavigationHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * Sets up the selected item in the nav bar with callback functions for each item.
     * @param navigationView The navigation bar to be set up.
     * @param currentNavBarItemId The ID of the nav bar item corresponding to the calling activity.
     */
    public void setupBottomNavigation(BottomNavigationView navigationView, int currentNavBarItemId) {
        // Set selected item in nav bar
        navigationView.setSelectedItemId(currentNavBarItemId);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem;

        // Set on-click listeners for menu items
        // Does not set an on-click listener for the menu item corresponding to the current activity
        for(Map.Entry<Integer, Class<?>> entry : PresentationConfig.activityMap.entrySet()) {
            int resourceId = entry.getKey();

            if(resourceId != currentNavBarItemId) {
                Class<?> targetActivity = entry.getValue();
                menuItem = menu.findItem(resourceId);

                menuItem.setOnMenuItemClickListener(item -> {
                    navigateToActivity(targetActivity);
                    return true;
                });
            }
        }
    }

    /**
     * Create an intent with the passed activity, and start the activity with the created intent.
     * @param targetActivity The activity to be started.
     */
    public void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        
        // reuse existing activity if it is already running
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        
        ActivityOptions options = ActivityOptions.makeCustomAnimation(activity, 0, 0);
        activity.startActivity(intent, options.toBundle());
    }
}