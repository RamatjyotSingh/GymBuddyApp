package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessWorkoutSessions;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;

public class WorkoutLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_workout_log);

        SearchView searchView = findViewById(R.id.workoutLogSearchView);
        searchView.setQueryHint(getString(R.string.workout_log_search_hint));

        RecyclerView recyclerView = findViewById(R.id.workoutLogRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AccessWorkoutSessions accessWorkoutSessions = new AccessWorkoutSessions();
        WorkoutLogAdapter logAdapter = new WorkoutLogAdapter(accessWorkoutSessions.getAll());
        recyclerView.setAdapter(logAdapter);
    }
}
