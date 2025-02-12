package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Set;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.IWorkoutSessionPersistence;

public class WorkoutSessionStub implements IWorkoutSessionPersistence {
    final private List<WorkoutSession> sessions;

    public WorkoutSessionStub(){
        sessions = new ArrayList<>();

        List<WorkoutProfile> profiles = Services.getWorkoutProfilePersistence().getAllWorkoutProfiles();
        List<Set> sets = Services.getSetPersistence().getAllSets();
        Date now = new Date();
        Random rand = new Random();

        sessions.add(new WorkoutSession(now.getTime(), 100*(rand.nextFloat()+1), sets, profiles.get(0)));
    }

    public List<WorkoutSession> getAllSessions() {
        return Collections.unmodifiableList(sessions);
    }
}
