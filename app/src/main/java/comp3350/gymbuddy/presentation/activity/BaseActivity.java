package comp3350.gymbuddy.presentation.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import comp3350.gymbuddy.presentation.util.PresentationConfig;

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
        for(Map.Entry<Integer, Class<?>> entry : PresentationConfig.activityMap.entrySet()){
            int resourceId = entry.getKey();

            if(resourceId != currentNavBarItemId){
                Class<?> activity = entry.getValue();
                menuItem = menu.findItem(resourceId);

                menuItem.setOnMenuItemClickListener(item -> {
                   navigateToActivity(activity);
                   return true;
                });
            }
        }
    }

    /**
     * Create an intent with the passed activity, and start the activity with the created intent.
     * @param activity The activity to be started.
     */
    private void navigateToActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        
        // resuse existing activity if it is already running
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        
        Bundle options = ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle();
        startActivity(intent, options);
    }
}