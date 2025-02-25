package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;

public class WorkoutItemTest {
    @Test
    public void testWorkoutItem(){
        WorkoutItem item;

        Exercise exercise = new Exercise(0, null, null, null, null);

        item = new WorkoutItem(exercise, 10);
        assertNotNull(item);
        assertEquals(exercise, item.getExercise());
        assertEquals(10, item.getSets());
    }
}
