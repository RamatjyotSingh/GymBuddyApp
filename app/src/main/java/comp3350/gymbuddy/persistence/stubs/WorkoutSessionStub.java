package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.logic.services.WorkoutProfileService;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public class WorkoutSessionStub implements IWorkoutSessionPersistence {
    final private List<WorkoutSession> sessions;

    public WorkoutSessionStub(){
        sessions = new ArrayList<>();

        var workoutProfileStub = new WorkoutProfileStub();
        var sessionItemStub = new SessionItemStub();

        List<WorkoutProfile> profiles = workoutProfileStub.getAll();
        List<SessionItem> sessionItems = sessionItemStub.getAll();
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

    public WorkoutSession getByStartTime(long startTime){
        WorkoutSession result = null;

        for(var i : sessions){
            if(i.getStartTime() == startTime){
                result = i;
            }
        }

        return result;
    }
}
