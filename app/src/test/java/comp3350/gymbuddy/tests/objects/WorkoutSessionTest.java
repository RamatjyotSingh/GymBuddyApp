package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;

public class WorkoutSessionTest {
    @Test
    public void testWorkoutSession(){
        WorkoutSession session;

        List<WorkoutItem> workoutItemList = new ArrayList<>();
        workoutItemList.add(new WorkoutItem(null, 100, 10));
        workoutItemList.add(new WorkoutItem(null, 5, 50.0));

        WorkoutProfile profile = new WorkoutProfile("Profile 1", "path", null);

        session = new WorkoutSession(1,100, 110, workoutItemList, profile);
        assertNotNull(session);
        assertEquals(100, session.getStartTime());
        assertEquals(10, session.getDuration(), 0);
        assertEquals(workoutItemList, session.getSessionItems());
        assertEquals(profile, session.getWorkoutProfile());
    }
}
