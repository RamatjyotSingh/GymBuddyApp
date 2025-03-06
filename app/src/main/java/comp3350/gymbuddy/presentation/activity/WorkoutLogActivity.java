package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding;
import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;

public class WorkoutLogActivity extends BaseActivity {
    // View binding for accessing UI elements efficiently
    private ActivityWorkoutLogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        // Inflate the layout using view binding
        binding = ActivityWorkoutLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.workoutLogSearchView.setQueryHint(getString(R.string.workout_log_search_hint));

        binding.workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from persistence
        var workoutSessionManager = new WorkoutSessionManager(true);
        List<WorkoutSession> sessions = new ArrayList<>();
        try {
            sessions = workoutSessionManager.getAll();
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        var logAdapter = new WorkoutLogAdapter(sessions, this::openWorkoutLogDetail);
        binding.workoutLogRecyclerView.setAdapter(logAdapter);

        setupBottomNavigation(binding.bottomNavigationView);
    }

    private void openWorkoutLogDetail(WorkoutSession workoutSession){
        Intent intent = new Intent(this, WorkoutLogDetailActivity.class);
        intent.putExtra("workoutSessionId", workoutSession.getId());
        startActivity(intent);
    }
}
