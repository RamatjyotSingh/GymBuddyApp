package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the exercise list and adapter
        AccessExercises accessExercises = new AccessExercises();
        List<Exercise> exerciseList = accessExercises.getAllExercises();

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this, exerciseList, this::openExerciseDetail);
        recyclerView.setAdapter(exerciseAdapter);
        SearchView searchView = findViewById(R.id.searchView);
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
    }

    private void openExerciseDetail(Exercise exercise) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("title", exercise.getName());
        if(exercise.getImagePath() != null){
            intent.putExtra("imagePath", "images/" + exercise.getImagePath());
        }
        intent.putStringArrayListExtra("instructions", (ArrayList<String>)exercise.getInstructions());  // Pass instructions

        // Convert tags to String List
        List<String> tagsList = new ArrayList<>();
        for (Tag tag : exercise.getTags()) {
            tagsList.add(tag.getName());
        }
        intent.putStringArrayListExtra("tagNames", (ArrayList<String>)tagsList);

        List<String> colorsList = new ArrayList<>();
        for (Tag tag : exercise.getTags()) {
            colorsList.add(tag.getColor());
        }
        intent.putStringArrayListExtra("tagColors", (ArrayList<String>)colorsList);

        startActivity(intent);
    }
}
