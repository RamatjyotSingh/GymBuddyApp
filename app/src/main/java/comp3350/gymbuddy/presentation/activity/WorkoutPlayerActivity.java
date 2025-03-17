package comp3350.gymbuddy.presentation.activity;

import android.app.Activity;
import android.os.Bundle;

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
}
