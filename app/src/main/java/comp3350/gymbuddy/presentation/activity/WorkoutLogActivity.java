package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.exception.BusinessException;
import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;
import comp3350.gymbuddy.presentation.util.ErrorHandler;
import comp3350.gymbuddy.presentation.util.NavigationHelper;
import comp3350.gymbuddy.presentation.util.ToastErrorDisplay;

public class WorkoutLogActivity extends AppCompatActivity {
    private final ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(this));

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        
        // Initialize navigation helper
        NavigationHelper navigationHelper = new NavigationHelper(this);
        
        // Inflate the layout using view binding
        // View binding for accessing UI elements efficiently
        comp3350.gymbuddy.databinding.ActivityWorkoutLogBinding binding = ActivityWorkoutLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from persistence using ApplicationService
        List<WorkoutSession> sessions;
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
        } catch(BusinessException e){
            handler.handle(e, getString(R.string.error_loading_workout_log));
        }

        // Use the navigation helper instead of inherited method
        navigationHelper.setupBottomNavigation(binding.bottomNavigationView, R.id.workout_log);
    }

    private void openWorkoutLogDetail(WorkoutSession workoutSession){
        Intent intent = new Intent(this, WorkoutLogDetailActivity.class);
        intent.putExtra(getString(R.string.intent_workout_session_id), workoutSession.getId());
        startActivity(intent);
    }
}
