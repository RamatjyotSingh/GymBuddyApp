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
import comp3350.gymbuddy.logic.services.ExerciseService;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;
import comp3350.gymbuddy.presentation.fragments.AddExerciseBottomSheetFragment;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        // Initialize the exercise list and adapter
        ExerciseService exerciseService = new ExerciseService();
        List<Exercise> exerciseList = exerciseService.getAll();

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
                var exerciseService = new ExerciseService();
                exerciseAdapter.setExercises(exerciseService.search(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                var exerciseService = new ExerciseService();
                exerciseAdapter.setExercises(exerciseService.search(newText));
                return false;
            }
        });
    }

    private void onExerciseClicked(Exercise exercise) {
        // Send exercise data back to the previous activity (WorkoutBuilderActivity)
        Intent intent = new Intent();
        intent.putExtra("exerciseID", exercise.getID());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onViewMoreClicked(Exercise exercise) {
        // Open ExerciseDetailActivity to show exercise details.
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("exerciseID", exercise.getID());
        startActivity(intent);
    }
}
