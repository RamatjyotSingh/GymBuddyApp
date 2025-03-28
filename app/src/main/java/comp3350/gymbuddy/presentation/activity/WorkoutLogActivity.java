package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;
import comp3350.gymbuddy.presentation.util.NavigationHelper;
import timber.log.Timber;

public class WorkoutLogActivity extends AppCompatActivity {
    // View binding for accessing UI elements efficiently
    private ActivityWorkoutLogBinding binding;
    private NavigationHelper navigationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        
        // Initialize navigation helper
        navigationHelper = new NavigationHelper(this);
        
        // Inflate the layout using view binding
        binding = ActivityWorkoutLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from persistence using ApplicationService
        List<WorkoutSession> sessions = new ArrayList<>();
        try {
            WorkoutSessionManager workoutSessionManager = ApplicationService.getInstance().getWorkoutSessionManager();
            sessions = workoutSessionManager.getAll();
            
            WorkoutLogAdapter logAdapter = new WorkoutLogAdapter(sessions, this::openWorkoutLogDetail);
            binding.workoutLogRecyclerView.setAdapter(logAdapter);

            binding.workoutLogSearchView.setQueryHint(getString(R.string.workout_log_search_hint));
            binding.workoutLogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    logAdapter.setWorkoutSessions(workoutSessionManager.search(query));
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!newText.isBlank()){
                        logAdapter.setWorkoutSessions(workoutSessionManager.search(newText));
                    }
                    else{
                        logAdapter.setWorkoutSessions(workoutSessionManager.getAll());
                    }
                    return false;
                }
            });
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Application not properly initialized. Please restart the app.", Toast.LENGTH_LONG).show();
            Timber.e(e, "ApplicationService not initialized in WorkoutLogActivity");
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Timber.e(e, "Database error in WorkoutLogActivity");
        }

        // Use the navigation helper instead of inherited method
        navigationHelper.setupBottomNavigation(binding.bottomNavigationView, R.id.workout_log);
    }

    private void openWorkoutLogDetail(WorkoutSession workoutSession){
        Intent intent = new Intent(this, WorkoutLogDetailActivity.class);
        intent.putExtra("workoutSessionId", workoutSession.getId());
        startActivity(intent);
    }
}
