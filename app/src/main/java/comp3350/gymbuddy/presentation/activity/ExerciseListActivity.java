package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;

public class ExerciseListActivity extends SearchableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the exercise list and adapter
        AccessExercises accessExercises = new AccessExercises();
        List<Exercise> exerciseList = accessExercises.getAll();

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this, exerciseList, this::openExerciseDetail);
        recyclerView.setAdapter(exerciseAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                exerciseAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exerciseAdapter.filter(newText);
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.exercise_list_search_hint));
    }

    private void openExerciseDetail(Exercise exercise) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);

        intent.putExtra("exerciseID", exercise.getID());

        startActivity(intent);
    }
}
