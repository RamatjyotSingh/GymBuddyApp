package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import comp3350.gymbuddy.databinding.ActivityWorkoutPlayerBinding;

public class WorkoutPlayerActivity extends Activity {

    // View binding for accessing UI elements efficiently
    private ActivityWorkoutPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using view binding
        binding = ActivityWorkoutPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /**
     * Handles the Floating Action Button (FAB) click event.
     * Creates a new entry in the recycler view.
     */
    public void onClickFAB(View v) {

    }

    /**
     * Handles the Previous button click event.
     */
    public void onClickPrevious(View v) {

    }

    /**
     * Handles the Next button click event.
     */
    public void onClickNext(View v) {

    }
}
