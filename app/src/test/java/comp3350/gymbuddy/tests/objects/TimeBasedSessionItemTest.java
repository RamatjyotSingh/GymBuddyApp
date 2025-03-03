package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;

public class TimeBasedSessionItemTest {
    @Test
    public void testTimeBasedSessionItem() {
        // Mock an Exercise object to avoid null pointer errors
        Exercise mockExercise = new Exercise(0, "Plank", null, null, null, true, false);

        // Use a concrete subclass (TimeBasedWorkoutItem)
        WorkoutItem associatedItem = new TimeBasedWorkoutItem(mockExercise, 3, 60.0);

        // Create a TimeBasedSessionItem
        TimeBasedSessionItem item = new TimeBasedSessionItem(associatedItem, 100.0);

        // Assertions
        assertNotNull(item);
        assertEquals(associatedItem, item.getAssociatedWorkoutItem());
        assertEquals(100.0, item.getTime(), 0.01);
    }
}
