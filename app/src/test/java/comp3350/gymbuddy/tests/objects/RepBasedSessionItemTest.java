package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;


public class RepBasedSessionItemTest {
    @Test
    public void testRepBasedSessionItem(){
        SessionItem item;

        WorkoutItem associatedItem = new WorkoutItem(null, 0, 30.0);

        item = new SessionItem(associatedItem, 100, 10);
        assertNotNull(item);
        assertEquals(associatedItem, item.getAssociatedWorkoutItem());
        assertEquals(10, item.getWeight(), 0);
        assertEquals(100, item.getReps());
    }
}
