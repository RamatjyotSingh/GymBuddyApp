package comp3350.gymbuddy.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        comp3350.gymbuddy.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up BottomNavigationView using the base class
        setupBottomNavigation(binding.bottomNavigationView);

        RecyclerView recyclerViewWorkouts = binding.recyclerViewWorkouts;

        // Initialize AccessWorkoutProfiles
        AccessWorkoutProfiles accessWorkoutProfiles = new AccessWorkoutProfiles();

        // Fetch data from persistence
        List<WorkoutProfile> workoutProfiles = accessWorkoutProfiles.getAll();

        // Initialize RecyclerView
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter
        WorkoutProfileAdapter workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);
        recyclerViewWorkouts.setAdapter(workoutProfileAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}