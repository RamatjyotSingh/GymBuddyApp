package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.AccessWorkoutSessions;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.WorkoutSession;

public class WorkoutLogDetailActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_workout_log_detail);

        AccessWorkoutSessions accessWorkoutSessions = new AccessWorkoutSessions();
        WorkoutSession session = accessWorkoutSessions.getByID(getIntent().getIntExtra("workoutSessionID", 0));

        addSessionItems(session);

        TextView sessionDate = findViewById(R.id.txtSessionDate);
        TextView sessionProfile = findViewById(R.id.txtSessionProfile);

        sessionDate.setText(session.getDate());
        sessionProfile.setText(session.getWorkoutProfile().getName());
    }

    private void addSessionItems(WorkoutSession workoutSession){
        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayout insertPoint = findViewById(R.id.workoutLogDetailLayout);

        for(SessionItem item : workoutSession.getSessionItems()){
            View newView = layoutInflater.inflate(R.layout.item_workout_session_profile_item, null);
            Exercise associatedExercise = item.getAssociatedWorkoutItem().getExercise();

            newView.setId(item.getID()); // set the ID for on click actions

            TextView exerciseName = newView.findViewById(R.id.sessionItemExerciseName);
            exerciseName.setText(associatedExercise.getName());

            TextView exerciseInfo = newView.findViewById(R.id.sessionItemExerciseInfo);

            // build info string that appears under exercise name
            String infoString = getString(R.string.default_string);
            if(item instanceof RepBasedSessionItem){
                RepBasedSessionItem castedItem = (RepBasedSessionItem) item;
                infoString = castedItem.getReps() + " reps, " + castedItem.getWeight() + " lbs";
            }
            else if(item instanceof TimeBasedSessionItem){
                TimeBasedSessionItem castedItem = (TimeBasedSessionItem)item;
                infoString = castedItem.getTime() + " sec";
            }
            exerciseInfo.setText(infoString);

            // set margins so the list elements aren't so close together
            CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);

            newView.setLayoutParams(params);

            insertPoint.addView(newView);
        }
    }
}
