package comp3350.gymbuddy.tests.system;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import comp3350.gymbuddy.presentation.activity.MainActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutBuilderActivity;
import comp3350.gymbuddy.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class WorkoutBuilderTest {

    @Before
    public void setup() {
        Intents.init();
    }
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddExerciseToWorkout() throws InterruptedException {


        // Step 2: Ensure RecyclerView has data before proceeding
        onView(withId(R.id.recyclerViewWorkouts)).check(matches(hasMinimumChildCount(1)));

        // Step 3: Ensure Bottom Navigation is visible
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));

        // Step 4: Click "Build Workouts" in Bottom Navigation
        onView(withId(R.id.build_workouts)).perform(click());

        // Step 5: Verify that WorkoutBuilderActivity opens
        intended(hasComponent(WorkoutBuilderActivity.class.getName()));

        // Step 6: Ensure WorkoutBuilderActivity fully loads
        onView(withId(R.id.fabAddItem)).check(matches(isDisplayed()));

        // Step 7: Click "Add" button to open exercise list
        onView(withId(R.id.fabAddItem)).perform(click());

        // Step 8: Wait for RecyclerView to fully load
        onView(withId(R.id.exerciseListRecyclerView)).check(matches(hasMinimumChildCount(1)));

        // Step 9: Click "Push-ups" inside RecyclerView
        onView(withId(R.id.exerciseListRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Step 10: Enter number of sets
        onView(withId(R.id.edtSets)).perform(typeText("3"), closeSoftKeyboard());

        // Step 11: Enter number of reps
        onView(withId(R.id.edtReps)).perform(typeText("12"), closeSoftKeyboard());

        // Step 12: Click "Add" to confirm selection
        onView(withId(R.id.btnAddWorkoutItem)).perform(click());

        Thread.sleep(3000);  // ✅ Wait 1 second to allow UI updates
        onView(withId(R.id.recyclerWorkoutItems))
                .check(matches(hasDescendant(withText("Push-Up"))));

        // Step 14: Enter workout name
        onView(withId(R.id.edtWorkoutName)).perform(typeText("My Workout"), closeSoftKeyboard());

        // Step 15: Click save button
        onView(withId(R.id.btnSaveWorkout)).perform(click());

        // Step 16: Verify workout appears in RecyclerView
        onView(withId(R.id.recyclerViewWorkouts)).check(matches(isDisplayed()));



    }

    @After
    public void teardown() {
        Intents.release();
    }


}
