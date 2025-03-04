package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.WorkoutItem;


public class RepBasedSessionItemTest {
    @Test
    public void testRepBasedSessionItem(){
        RepBasedSessionItem item;

        WorkoutItem associatedItem = new WorkoutItem(null, 0, 30.0);

        item = new RepBasedSessionItem(associatedItem, 100, 10);
        assertNotNull(item);
        assertEquals(associatedItem, item.getAssociatedWorkoutItem());
        assertEquals(100, item.getWeight(), 0);
        assertEquals(10, item.getReps());
    }
}
