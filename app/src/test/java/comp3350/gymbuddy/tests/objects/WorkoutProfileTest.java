package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class WorkoutProfileTest {
    @Test
    public void testWorkoutProfile() {
        // Mock an Exercise object to avoid null pointer errors
        Exercise mockExercise = new Exercise(1, "Squat", null, null, null, false, true);

        // Use a concrete subclass (RepBasedWorkoutItem)
        List<WorkoutItem> itemList = new ArrayList<>();
        itemList.add(new WorkoutItem(mockExercise, 3, 12, 50.0));

        // Create the WorkoutProfile
        WorkoutProfile profile = new WorkoutProfile("Profile 1", "path", itemList);

        // Assertions
        assertNotNull(profile);
        assertEquals("Profile 1", profile.getName());
        assertEquals("path", profile.getIconPath());
        assertEquals(itemList, profile.getWorkoutItems());
    }
}
