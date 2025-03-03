package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityMainBinding;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding;
import comp3350.gymbuddy.logic.AccessWorkoutSessions;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;

public class WorkoutLogActivity extends AppCompatActivity {

    private ActivityWorkoutLogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        binding = ActivityWorkoutLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.workoutLogSearchView.setQueryHint(getString(R.string.workout_log_search_hint));

        binding.workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AccessWorkoutSessions accessWorkoutSessions = new AccessWorkoutSessions();
        WorkoutLogAdapter logAdapter = new WorkoutLogAdapter(accessWorkoutSessions.getAll(), this::openWorkoutLogDetail);
        binding.workoutLogRecyclerView.setAdapter(logAdapter);

        // Access views using binding
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        RecyclerView recyclerViewWorkouts = binding.workoutLogRecyclerView;

        // Set up BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.workout_log) {
                // Already on the workout log screen, do nothing
                return true;
            } else if (id == R.id.build_workouts) {
                // Navigate to WorkoutBuilderActivity
                Intent workoutIntent = new Intent(WorkoutLogActivity.this, WorkoutBuilderActivity.class);
                startActivity(workoutIntent);
                return true;
            } else if (id == R.id.home) {
                // Navigate to Home
                Intent workoutIntent = new Intent(WorkoutLogActivity.this, MainActivity.class);
                startActivity(workoutIntent);
                return true;
            }
            return false;
        });

        // Highlight the active menu item
        bottomNavigationView.setSelectedItemId(R.id.workout_log);

    }

    private void openWorkoutLogDetail(WorkoutSession workoutSession){
        Intent intent = new Intent(this, WorkoutLogDetailActivity.class);
        intent.putExtra("workoutSessionStartTime", workoutSession.getStartTime());
        startActivity(intent);
    }
}
