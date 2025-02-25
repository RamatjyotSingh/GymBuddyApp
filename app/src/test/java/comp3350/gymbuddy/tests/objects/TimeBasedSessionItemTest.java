package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.WorkoutItem;

public class TimeBasedSessionItemTest {
    @Test
    public void testTimeBasedSessionItem(){
        TimeBasedSessionItem item;

        WorkoutItem associatedItem = new WorkoutItem(null, 0);

        item = new TimeBasedSessionItem(associatedItem, 100);
        assertNotNull(item);
        assertEquals(associatedItem, item.getAssociatedWorkoutItem());
        assertEquals(100, item.getTime(), 0);
    }
}
