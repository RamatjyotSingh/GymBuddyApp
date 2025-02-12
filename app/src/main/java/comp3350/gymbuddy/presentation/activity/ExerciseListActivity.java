package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.presentation.adapters.ExerciseAdapter;
import comp3350.gymbuddy.presentation.fragments.AddExerciseBottomSheetFragment;
import comp3350.gymbuddy.logic.AccessExercises;

public class ExerciseListActivity extends AppCompatActivity {
    private List<Exercise> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the exercise list and adapter
        AccessExercises accessExercises = new AccessExercises();
        exerciseList = accessExercises.getAllExercises();

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this, exerciseList, this::openExerciseDetail);
        recyclerView.setAdapter(exerciseAdapter);
    }

    private void openExerciseDetail(Exercise exercise) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("title", exercise.getName());
        if(exercise.getImagePath() != null){
            intent.putExtra("imagePath", "images/" + exercise.getImagePath());
        }
        intent.putExtra("instructions", exercise.getInstructions());  // Pass instructions

        // Convert tags to String List
        ArrayList<String> tagsList = new ArrayList<>();
        for (Tag tag : exercise.getTags()) {
            tagsList.add(tag.getName());
        }
        intent.putStringArrayListExtra("tagNames", tagsList);

        ArrayList<String> colorsList = new ArrayList<>();
        for (Tag tag : exercise.getTags()) {
            colorsList.add(tag.getColor());
        }
        intent.putStringArrayListExtra("tagColors", colorsList);

        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            String exerciseName = data.getStringExtra("exerciseName");
            String exerciseInstructions = data.getStringExtra("exerciseInstructions");
            String exerciseImage = data.getStringExtra("exerciseImage");

            ArrayList<String> tagsList = data.getStringArrayListExtra("tagNames");
            ArrayList<String> colorsList = data.getStringArrayListExtra("tagColors");


        }
    }


}
