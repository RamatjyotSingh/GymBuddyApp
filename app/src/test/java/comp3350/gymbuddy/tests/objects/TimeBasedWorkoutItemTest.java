package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;

public class TimeBasedWorkoutItemTest {
    @Test
    public void testTimeBasedWorkoutItem() {
        Exercise exercise = new Exercise(0, null, null, null, null,false,false);

        WorkoutItem item = new WorkoutItem(exercise, 100, 100.0);
        assertEquals(exercise, item.getExercise());
        assertEquals(100, item.getSets());
        assertEquals(100, item.getTime(), 0.01);
    }
}
