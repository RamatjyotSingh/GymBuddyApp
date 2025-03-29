package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;

public class WorkoutItemTest {
    @Test
    public void testRepBasedWorkoutItem() {
        // Create an Exercise object
        Exercise exercise = new Exercise(0, "Push-Up", null, null, null, false, false);

        WorkoutItem item = new WorkoutItem(exercise, 3, 12, 50.0);

        // Assertions
        assertEquals(exercise, item.getExercise());
        assertEquals(3, item.getSets());
    }

    @Test
    public void testTimeBasedWorkoutItem() {
        // Create an Exercise object
        Exercise exercise = new Exercise(1, "Plank", null, null, null, true, false);

        WorkoutItem item = new WorkoutItem(exercise, 1, 60.0);

        // Assertions
        assertNotNull(item);
        assertEquals(exercise, item.getExercise());
        assertEquals(1, item.getSets());
        assertEquals(60.0, item.getTime(), 0.01);
    }
}
