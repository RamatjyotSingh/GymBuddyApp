package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private WorkoutProfileAdapter workoutProfileAdapter;
    private List<WorkoutProfile> workoutProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up BottomNavigationView using the base class
        setupBottomNavigation(binding.bottomNavigationView);

        // Initialize RecyclerView
        RecyclerView recyclerViewWorkouts = binding.recyclerViewWorkouts;
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data structures
        workoutProfiles = new ArrayList<>();
        workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);
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
            Toast.makeText(this, "Error loading workout profiles: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Scrolls the RecyclerView to show a specific workout profile
     */
    private void scrollToProfile(int profileId) {
        // Find the position of the profile with the given ID
        for (int i = 0; i < workoutProfiles.size(); i++) {
            if (workoutProfiles.get(i).getID() == profileId) {
                // Scroll to the position
                binding.recyclerViewWorkouts.smoothScrollToPosition(i);
                
                // Optionally highlight the item
                workoutProfileAdapter.highlightItem(i);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity (alternative to intent-based approach)
        loadWorkoutProfiles();
    }
}