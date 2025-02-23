package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;

public class RepBasedWorkoutItemTest {
    @Test
    public void testRepBasedWorkoutItem(){
        RepBasedWorkoutItem item;

        Exercise exercise = new Exercise(0, null, null, null, null);

        item = new RepBasedWorkoutItem(exercise, 100, 10, 100);
        assertNotNull(item);
        assertEquals(exercise, item.getExercise());
        assertEquals(100, item.getSets());
        assertEquals(10, item.getReps());
        assertEquals(100, item.getWeight(), 0);
    }
}
