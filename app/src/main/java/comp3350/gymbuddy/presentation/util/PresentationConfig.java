package comp3350.gymbuddy.presentation.util;

import java.util.Map;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.presentation.activity.MainActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutBuilderActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutLogActivity;

public class PresentationConfig {
    // Maps nav bar menu item IDs to subclasses of BaseActivity
    public final static Map<Integer, Class<?>> activityMap = Map.ofEntries(
        Map.entry(R.id.home, MainActivity.class),
        Map.entry(R.id.build_workouts, WorkoutBuilderActivity.class),
        Map.entry(R.id.workout_log, WorkoutLogActivity.class)
    );
}
