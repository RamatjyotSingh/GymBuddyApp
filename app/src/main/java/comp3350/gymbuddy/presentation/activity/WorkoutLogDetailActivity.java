package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogDetailBinding;
import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;

public class WorkoutLogDetailActivity extends AppCompatActivity{
    private ActivityWorkoutLogDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        binding = ActivityWorkoutLogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the session ID passed from WorkoutLogActivity.
        int id = getIntent().getIntExtra("workoutSessionId", 0);

        // Get session details from persistence.
        var workoutSessionManager = new WorkoutSessionManager(true);
        WorkoutSession session = null;
        try {
            session = workoutSessionManager.getWorkoutSessionById(id);
        } catch (DBException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        setWorkoutSession(session);
    }

    private void setWorkoutSession(WorkoutSession session) {
        if (session != null) {
            // Set session date & name.
            binding.txtSessionDate.setText(session.getDate());
            binding.txtSessionProfile.setText(session.getWorkoutProfile().getName());

            // Update workout items.
            addWorkoutItems(session);
        }
    }

    private void addWorkoutItems(WorkoutSession workoutSession) {
        if (workoutSession != null) {
            LayoutInflater layoutInflater = getLayoutInflater();
            LinearLayout insertPoint = binding.workoutLogDetailLayout;

            for (var item : workoutSession.getWorkoutItems()) {
                View newView = layoutInflater.inflate(R.layout.item_workout_session_profile_item, binding.getRoot());

                // Get the associated exercise.
                Exercise exercise = item.getExercise();

                // set text views on list element
                TextView exerciseName = newView.findViewById(R.id.workoutItemExerciseName);
                exerciseName.setText(exercise.getName());

                TextView exerciseInfo = newView.findViewById(R.id.workoutItemExerciseInfo);
                exerciseInfo.setText(item.toString());

                // set margins on the list elements so they aren't so close together
                CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 8, 8, 8);

                newView.setLayoutParams(params);

                insertPoint.addView(newView);
            }
        }
    }
}
