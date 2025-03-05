package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.databinding.ActivityWorkoutLogDetailBinding;
import comp3350.gymbuddy.logic.services.WorkoutSessionService;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutSession;

public class WorkoutLogDetailActivity extends AppCompatActivity{
    private ActivityWorkoutLogDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        binding = ActivityWorkoutLogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // get associated workout session from intent object
        WorkoutSessionService workoutSessionService = new WorkoutSessionService();
        WorkoutSession session = workoutSessionService.getByStartTime(getIntent().getLongExtra("workoutSessionStartTime", 0));

        addSessionItems(session);

        // set main text views
        TextView sessionDate = binding.txtSessionDate;
        TextView sessionProfile = binding.txtSessionProfile;

        sessionDate.setText(session.getDate());
        sessionProfile.setText(session.getWorkoutProfile().getName());
    }

    private void addSessionItems(WorkoutSession workoutSession){
        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayout insertPoint = binding.workoutLogDetailLayout;

        for(SessionItem item : workoutSession.getSessionItems()){
            View newView = layoutInflater.inflate(R.layout.item_workout_session_profile_item, null);
            Exercise associatedExercise = item.getAssociatedWorkoutItem().getExercise();

            // set text views on list element
            TextView exerciseName = newView.findViewById(R.id.sessionItemExerciseName);
            exerciseName.setText(associatedExercise.getName());

            TextView exerciseInfo = newView.findViewById(R.id.sessionItemExerciseInfo);
            exerciseInfo.setText(item.toString());

            // set margins on the list elements so they aren't so close together
            CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);

            newView.setLayoutParams(params);

            insertPoint.addView(newView);
        }
    }
}
