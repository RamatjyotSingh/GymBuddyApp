package comp3350.gymbuddy.tests.system;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertNotNull;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.util.StringFormatter;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.activity.MainActivity;
import comp3350.gymbuddy.presentation.activity.StartWorkoutListActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutPlayerActivity;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;
import comp3350.gymbuddy.tests.system.matchers.WorkoutItemMatchers;

@RunWith(AndroidJUnit4.class)
public class WorkoutPlayerTest {
    // The index of the workout to pick from the list
    private static final int SELECTION_INDEX = 0;

    // This is the workout being selected. Used for verifying the contents of the view data
    WorkoutProfile selectedWorkout;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        Intents.init();

        // Retrieve the workout model object from RecyclerView using ActivityScenario
        activityRule.getScenario().onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.recyclerViewWorkouts);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                var adapter = (WorkoutProfileAdapter)recyclerView.getAdapter();
                selectedWorkout = adapter.getCurrentList().get(SELECTION_INDEX);
            }
        });
    }

    @After
    public void teardown(){
        Intents.release();
    }

    /**
     * Tests the screen with all the exercises before starting a workout.
     * Ensures the exercises match what is expected.
     */
    @Test
    public void testViewWorkoutDetails() {
        // Check that the target workout exists in the list
        onView(withId(R.id.recyclerViewWorkouts))
                .check(matches(hasMinimumChildCount(SELECTION_INDEX)));

        // Ensure the profile was retrieved
        assertNotNull("Failed to retrieve workout profile", selectedWorkout);

        // Get the workout items from the profile
        List<WorkoutItem> workoutItems = selectedWorkout.getWorkoutItems();

        // Click on workout
        onView(withId(R.id.recyclerViewWorkouts))
                .perform(actionOnItemAtPosition(SELECTION_INDEX, click()));

        // Check that StartWorkoutListActivity has been launched after click
        intended(hasComponent(StartWorkoutListActivity.class.getName()));

        // Make sure the workout name is correct
        onView(withId(R.id.workoutName))
                .check(matches(withText(selectedWorkout.getName())));

        // Make sure the workout items match what's being displayed
        onView(withId(R.id.exerciseRecyclerView))
                .check(matches(WorkoutItemMatchers.matchesWorkoutItems(workoutItems)));
    }

    /**
     * Tests the screen for playing through workouts. Ensures all the information displayed is as
     * expected.
     */
    @Test
    public void testPlayThroughWorkout() {
        // Check that the target workout exists in the list
        onView(withId(R.id.recyclerViewWorkouts))
                .check(matches(hasMinimumChildCount(SELECTION_INDEX)));

        // Ensure the profile was retrieved
        assertNotNull("Failed to retrieve workout profile", selectedWorkout);

        // Get the workout items from the profile
        List<WorkoutItem> workoutItems = selectedWorkout.getWorkoutItems();

        // Click on workout
        onView(withId(R.id.recyclerViewWorkouts))
                .perform(actionOnItemAtPosition(SELECTION_INDEX, click()));

        // Check that StartWorkoutListActivity has been launched after click
        intended(hasComponent(StartWorkoutListActivity.class.getName()));

        // Click start
        onView(withId(R.id.buttonStartWorkout))
                .perform(click());

        // Check that WorkoutPlayerActivity has been launched after click
        intended(hasComponent(WorkoutPlayerActivity.class.getName()));

        // Make sure the workout name is correct
        onView(withId(R.id.tvWorkoutName))
                .check(matches(withText(selectedWorkout.getName())));

        // Click through each exercise and verify that the information shown is correct
        for (var workoutItem : workoutItems) {
            checkWorkoutItemInfo(workoutItem);

            // Click next
            onView(withId(R.id.btnNext))
                    .perform(click());
        }

        // Now we should be at the StartWorkoutListActivity
        intended(hasComponent(StartWorkoutListActivity.class.getName()));
    }

    /**
     * Checks if the player screen is currently showing matching information to the expected
     * workout item
     * @param expectedWorkoutItem the expected workout item to be shown
     */
    private void checkWorkoutItemInfo(WorkoutItem expectedWorkoutItem) {
        // Make sure the exercise name is correct
        onView(withId(R.id.tvExerciseName))
                .check(matches(withText(expectedWorkoutItem.getExercise().getName())));

        // Make sure the sets are correct
        onView(withId(R.id.tvSets))
                .check(matches(withText(Integer.toString(expectedWorkoutItem.getSets()))));

        // Create a formatter to generate the expected text
        var formatter = new StringFormatter();

        if (expectedWorkoutItem.isTimeBased()) {
            // Check that the time is shown
            checkInfoHeader(R.id.timeLayout, R.id.tvTime,
                    formatter.formatTime(expectedWorkoutItem.getTime()));
        } else {
            // Check that the reps are shown
            checkInfoHeader(R.id.repsLayout, R.id.tvReps,
                    Integer.toString(expectedWorkoutItem.getReps()));

            if (expectedWorkoutItem.hasWeight()) {
                // Check that the weight is shown
                checkInfoHeader(R.id.weightLayout, R.id.tvWeight,
                        formatter.formatWeight(expectedWorkoutItem.getWeight()));
            }
        }
    }

    /**
     * Checks that an individual header is visible and includes the expected information.
     * @param header the resource ID of the header to check
     * @param textView the resource ID of the text view to check
     * @param expectedText the expected text in the text view
     */
    private void checkInfoHeader(int header, int textView, String expectedText) {
        // Check that the header is visible
        onView(withId(header))
                .check(matches(isCompletelyDisplayed()));

        // Check that the information is correct
        onView(withId(textView))
                .check(matches(withText(expectedText)));
    }
}
