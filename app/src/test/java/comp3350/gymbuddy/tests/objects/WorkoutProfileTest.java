package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class WorkoutProfileTest {
    @Test
    public void testWorkoutProfile(){
        WorkoutProfile profile;

        List<WorkoutItem> itemList = new ArrayList<>();
        itemList.add(new WorkoutItem(null, 10));

        profile = new WorkoutProfile("Profile 1", "path", itemList);
        assertNotNull(profile);
        assertEquals("Profile 1", profile.getName());
        assertEquals("path", profile.getIconPath());
        assertEquals(itemList, profile.getWorkoutItems());
    }
}
