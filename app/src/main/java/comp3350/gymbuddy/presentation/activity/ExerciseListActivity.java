package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;
import timber.log.Timber;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);


        // Fetch data from persistence
        List<Exercise> exerciseList;
        ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();


        try {
            exerciseList = exerciseManager.getAll();

            // Initialize recycler view
            RecyclerView recyclerView = findViewById(R.id.exerciseListRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize adapter
            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exerciseList, this::onExerciseClicked, this::onViewMoreClicked);
            recyclerView.setAdapter(exerciseAdapter);

            // Initialize search bar
            SearchView searchView = findViewById(R.id.exerciseListSearchView);
            searchView.setQueryHint(getString(R.string.exercise_list_search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    exerciseAdapter.setExercises(exerciseManager.search(query));
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    exerciseAdapter.setExercises(exerciseManager.search(newText));
                    return false;
                }
            });
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Application not properly initialized. Please restart the app.", Toast.LENGTH_LONG).show();
            Timber.e(e, "ApplicationService not initialized in ExerciseListActivity");
            finish();
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Timber.e(e, "Database error in ExerciseListActivity: %s", e.getMessage());
        }
    }

    private void onExerciseClicked(Exercise exercise) {
        // Send exercise data back to the previous activity (WorkoutBuilderActivity)
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.exerciseid), exercise.getID());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onViewMoreClicked(Exercise exercise) {
        // Open ExerciseDetailActivity to show exercise details.
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra(getString(R.string.exerciseid), exercise.getID());
        startActivity(intent);
    }
}
