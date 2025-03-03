package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.logic.AccessSessionItems;
import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public class WorkoutSessionStub implements IWorkoutSessionPersistence {
    private static final long MAX_SESSION_LENGTH = 1000 * 60 * 60 * 2; // 2 hours
    private final List<WorkoutSession> sessions;
    private int nextId; // Simulated ID counter

    public WorkoutSessionStub(){
        sessions = new ArrayList<>();
        nextId = 1; // Start session ID

        AccessWorkoutProfiles accessWorkoutProfiles = new AccessWorkoutProfiles();
        AccessSessionItems accessSessionItems = new AccessSessionItems();

        List<WorkoutProfile> profiles = accessWorkoutProfiles.getAll();
        List<SessionItem> sessionItems = accessSessionItems.getAll();
        Date now = new Date();
        Random rand = new Random();

        // Ensure profiles exist and prevent negative session duration
        if (!profiles.isEmpty()) {
            long duration = Math.abs(rand.nextLong()) % WorkoutSession.MAX_SESSION_LENGTH;
            sessions.add(new WorkoutSession(now.getTime(), now.getTime() + duration, sessionItems, profiles.get(0)));
        }
    }

    @Override
    public void insertWorkoutSession(WorkoutSession session) {
        if (session != null) {
            sessions.add(session);
        }
    }

    @Override
    public List<WorkoutSession> getAll(){
        return Collections.unmodifiableList(sessions);
    }
}
