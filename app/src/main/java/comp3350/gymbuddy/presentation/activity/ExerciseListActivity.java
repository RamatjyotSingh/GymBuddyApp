package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.exception.BusinessException;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.presentation.util.ErrorHandler;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;
import comp3350.gymbuddy.presentation.util.ToastErrorDisplay;

public class ExerciseListActivity extends AppCompatActivity {
    private final ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        // Fetch data from persistence
        List<Exercise> exerciseList;

        try {
            ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();
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
        }
        catch (BusinessException e) {
            handler.handle(e, getString(R.string.error_loading_exercise));
        }
    }

    /**
     * Called when an exercise has been clicked.
     * @param exercise the exercise clicked
     */
    private void onExerciseClicked(Exercise exercise) {
        // Send exercise data back to the previous activity (WorkoutBuilderActivity)
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.exerciseid), exercise.getID());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the "View More" button has been clicked on an exercise.
     * @param exercise the exercise clicked
     */
    private void onViewMoreClicked(Exercise exercise) {
        // Open ExerciseDetailActivity to show exercise details.
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra(getString(R.string.exerciseid), exercise.getID());
        startActivity(intent);
    }
}
