package comp3350.gymbuddy.tests.objects;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;

public class WorkoutSessionTest {
    @Test
    public void testWorkoutSession(){
        WorkoutSession session;

        List<SessionItem> sessionItemList = new ArrayList<>();
        sessionItemList.add(new RepBasedSessionItem(null, 100, 10));
        sessionItemList.add(new TimeBasedSessionItem(null, 10));

        WorkoutProfile profile = new WorkoutProfile("Profile 1", "path", null);

        session = new WorkoutSession(100, 10, sessionItemList, profile);
        assertNotNull(session);
        assertEquals(100, session.getTimestamp());
        assertEquals(10, session.getDuration(), 0);
        assertEquals(sessionItemList, session.getSessionItems());
        assertEquals(profile, session.getWorkoutProfile());
    }
}
