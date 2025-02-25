package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.IWorkoutSessionPersistence;

public class WorkoutSessionStub implements IWorkoutSessionPersistence {
    final private List<WorkoutSession> sessions;

    public WorkoutSessionStub(){
        sessions = new ArrayList<>();

        List<WorkoutProfile> profiles = Services.getWorkoutProfilePersistence().getAll();
        List<SessionItem> sessionItems = Services.getSessionItemPersistence().getAll();
        Date now = new Date();
        Random rand = new Random();

        sessions.add(new WorkoutSession(now.getTime(), now.getTime() + (rand.nextLong() % MAX_SESSION_LENGTH), sessionItems, profiles.get(0)));
    }

    public List<WorkoutSession> getAll(){
        return Collections.unmodifiableList(sessions);
    }
}
