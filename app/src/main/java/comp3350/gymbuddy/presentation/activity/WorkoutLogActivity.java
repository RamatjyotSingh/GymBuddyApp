package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessWorkoutSessions;
import comp3350.gymbuddy.presentation.adapters.WorkoutLogAdapter;

public class WorkoutLogActivity extends SearchableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);

        setSearchView(R.layout.fragment_workout_log_search_bar);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.workout_log_search_hint));

        AccessWorkoutSessions accessWorkoutSessions = new AccessWorkoutSessions();
        WorkoutLogAdapter logAdapter = new WorkoutLogAdapter(accessWorkoutSessions.getAll());
        recyclerView.setAdapter(logAdapter);
    }
}
