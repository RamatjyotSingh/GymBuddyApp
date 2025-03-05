package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;

public class TimeBasedSessionItemTest {
    @Test
    public void testTimeBasedSessionItem() {
        // Mock an Exercise object to avoid null pointer errors
        Exercise mockExercise = new Exercise(0, "Plank", null, null, null, true, false);

        // Use a concrete subclass
        WorkoutItem associatedItem = new WorkoutItem(mockExercise, 3, 60.0);

        // Create a TimeBasedSessionItem
        SessionItem item = new SessionItem(associatedItem, 100.0);

        // Assertions
        assertNotNull(item);
        assertEquals(associatedItem, item.getAssociatedWorkoutItem());
        assertEquals(100.0, item.getTime(), 0.01);
    }
}
