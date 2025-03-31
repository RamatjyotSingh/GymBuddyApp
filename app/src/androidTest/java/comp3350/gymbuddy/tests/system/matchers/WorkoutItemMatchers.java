package comp3350.gymbuddy.tests.system.matchers;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.R;

/**
 * A collection of custom Expresso matchers which verify the contents of recycler views and workout
 * items.
 */
public class WorkoutItemMatchers {
    /**
     * Checks whether a RecyclerView contains a list of workout items that match the expected items.
     * Passes only if all items are identical.
     *
     * @param expectedItems the expected content of the list
     * @return custom Expresso matcher
     */
    public static Matcher<View> matchesWorkoutItems(List<WorkoutItem> expectedItems) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView should contain ")
                        .appendValue(expectedItems.size())
                        .appendText(" workout items");
            }

            @Override
            protected boolean matchesSafely(View item) {
                boolean matches = true;

                var recyclerView = (RecyclerView)item;

                // Ensure the number of items is the same.
                if (recyclerView.getChildCount() != expectedItems.size()) {
                    matches = false;
                }

                if (matches) {
                    // Ensure each view holder matches the expected list.
                    int i = 0;
                    while (matches && i < expectedItems.size()) {
                        WorkoutItem expectedItem = expectedItems.get(i);
                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);

                        if (viewHolder == null || !matchesWorkoutItem(expectedItem).matches(viewHolder.itemView)) {
                            matches = false;
                        }

                        i++;
                    }
                }

                return matches;
            }

            @Override
            public void describeMismatchSafely(View item, Description mismatchDescription) {
                // Make sure its a recycler view
                if (!(item instanceof RecyclerView)) {
                    mismatchDescription.appendText("was not a RecyclerView");
                    return;
                }

                RecyclerView recyclerView = (RecyclerView) item;
                int actualItemCount = recyclerView.getAdapter() != null
                        ? recyclerView.getAdapter().getItemCount()
                        : 0;

                mismatchDescription.appendText("RecyclerView had ")
                        .appendValue(actualItemCount)
                        .appendText(" items instead of ")
                        .appendValue(expectedItems.size());
            }
        };
    }

    /**
     * Checks whether an individual workout item matches the expected item.
     * @param expectedItem the expected workout item
     * @return custom Expresso matcher
     */
    private static Matcher<View> matchesWorkoutItem(WorkoutItem expectedItem) {
        return new BoundedMatcher<>(View.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Workout item should match: ")
                        .appendValue(expectedItem);
            }

            @Override
            protected boolean matchesSafely(View view) {
                // Get the views from the list item
                TextView txtName = view.findViewById(R.id.txtExerciseName);
                TextView txtDetails = view.findViewById(R.id.txtWorkoutDetails);

                // Ensure views exist and values match
                return txtName != null && txtDetails != null &&
                        txtName.getText().toString().equals(expectedItem.getExercise().getName()) &&
                        txtDetails.getText().toString().equals(expectedItem.toString());
            }
        };
    }
}
