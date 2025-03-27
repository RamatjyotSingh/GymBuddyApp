package comp3350.gymbuddy.tests.system;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.presentation.activity.MainActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutBuilderActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutLogActivity;
import comp3350.gymbuddy.presentation.activity.WorkoutLogDetailActivity;

@RunWith(AndroidJUnit4.class)
public class WorkoutLogTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        Intents.init();

        // TODO: Remove this once database initialization is removed from presentation layer
        // navigate to workout log
        onView(withId(R.id.workout_log))
                .perform(click());
    }

    @After
    public void teardown(){
        Intents.release();
    }

    @Test
    public void testViewWorkoutLogDetail(){
        // Check that there exists data in the log activity
        onView(withId(R.id.workoutLogRecyclerView))
                .check(matches(hasMinimumChildCount(1)));

        // Click on first item in RecyclerView
        onView(withId(R.id.workoutLogRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        // Check that WorkoutLogDetailActivity has been launched after click
        intended(hasComponent(WorkoutLogDetailActivity.class.getName()));

        // Check that there exists data in the detail activity
        onView(withId(R.id.workoutLogDetailLayout))
                .check(matches(hasMinimumChildCount(1)));

        // Exit the detail screen
        pressBack();

        // Check that WorkoutLogDetailActivity has been launched after click
        intended(hasComponent(WorkoutLogActivity.class.getName()));

        // Check that the nav bar is displayed.
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));

        // Check that there still exists data in the log activity
        onView(withId(R.id.workoutLogRecyclerView))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testSearchWorkoutLog() throws InterruptedException {
        // Check that the search bar is visible
        onView(withId(R.id.workoutLogSearchView))
                .check(matches(isDisplayed()));

        // Click on search bar
        onView(withId(R.id.workoutLogSearchView))
                .perform(click());

        // Type string into the internal EditText within the SearchView
        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(typeText("Full"));

        Thread.sleep(1000);  // ✅ Wait 1 second to allow UI updates

        // Check that there exists data in the log activity
        onView(withId(R.id.workoutLogRecyclerView))
                .check(matches(hasMinimumChildCount(1)));

        // Clear query
        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(clearText());

        // Search for a non-existent item
        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(typeText("/"));

        Thread.sleep(1000);  // ✅ Wait 1 second to allow UI updates

        // Check that there does not exist data in the log activity
        onView(withId(R.id.workoutLogRecyclerView))
                .check(matches(hasChildCount(0)));
    }

    private void navigate(int menuItemId, String className){
        // Check that the nav bar is displayed.
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));

        // Click on desired button in nav bar
        onView(withId(menuItemId))
                .perform(click());

        // Check that desired activity has been launched after click
        intended(hasComponent(className));

        // Check that the nav bar is displayed.
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToMainActivity(){
        navigate(R.id.home, MainActivity.class.getName());
    }

    @Test
    public void testNavigateToWorkoutBuilderActivity(){
        navigate(R.id.build_workouts, WorkoutBuilderActivity.class.getName());
    }
}
