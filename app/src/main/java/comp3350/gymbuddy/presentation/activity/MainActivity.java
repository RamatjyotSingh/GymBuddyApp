package comp3350.gymbuddy.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.database.DatabaseManager;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding; // View Binding
    private WorkoutProfileAdapter workoutProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabase();

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Access views using binding
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        RecyclerView recyclerViewWorkouts = binding.recyclerViewWorkouts;

        // Set up BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                // Already on the home screen, do nothing
                return true;
            } else if (id == R.id.build_workouts) {
                // Navigate to WorkoutBuilderActivity
                Intent workoutIntent = new Intent(MainActivity.this, WorkoutBuilderActivity.class);
                startActivity(workoutIntent);
                return true;
            } else if (id == R.id.workout_log) {
                // Navigate to WorkoutLogActivity
                Intent workoutIntent = new Intent(MainActivity.this, WorkoutLogActivity.class);
                startActivity(workoutIntent);
                return true;
            }
            return false;
        });

        // Highlight the active menu item
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Initialize AccessWorkoutProfiles
        AccessWorkoutProfiles accessWorkoutProfiles = new AccessWorkoutProfiles();

        // Fetch data from persistence
        List<WorkoutProfile> workoutProfiles = accessWorkoutProfiles.getAll();

        // Initialize RecyclerView
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter
        workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);
        recyclerViewWorkouts.setAdapter(workoutProfileAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset the selected item in the BottomNavigationView
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //this method helps to initialize the db
    private void initializeDatabase() {
        DatabaseManager.getInstance(this);
    }
}
