package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding;
import comp3350.gymbuddy.logic.AccessWorkoutSessions;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;

public class WorkoutLogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding binding = ActivityWorkoutLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.workoutLogSearchView.setQueryHint(getString(R.string.workout_log_search_hint));

        binding.workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AccessWorkoutSessions accessWorkoutSessions = new AccessWorkoutSessions();
        WorkoutLogAdapter logAdapter = new WorkoutLogAdapter(accessWorkoutSessions.getAll(), this::openWorkoutLogDetail);
        binding.workoutLogRecyclerView.setAdapter(logAdapter);

        // Access views using binding
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        RecyclerView recyclerViewWorkouts = binding.workoutLogRecyclerView;

        setupBottomNavigation(binding.bottomNavigationView);

    }

    private void openWorkoutLogDetail(WorkoutSession workoutSession){
        Intent intent = new Intent(this, WorkoutLogDetailActivity.class);
        intent.putExtra("workoutSessionStartTime", workoutSession.getStartTime());
        startActivity(intent);
    }
}
