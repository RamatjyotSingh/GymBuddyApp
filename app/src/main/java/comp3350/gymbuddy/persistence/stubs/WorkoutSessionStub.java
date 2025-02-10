package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.logic.SetService;
import comp3350.gymbuddy.logic.WorkoutProfileService;
import comp3350.gymbuddy.objects.Set;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.IWorkoutSessionPersistence;

public class WorkoutSessionStub implements IWorkoutSessionPersistence {
    private List<WorkoutSession> sessions;

    public WorkoutSessionStub(){
        this.sessions = new ArrayList<WorkoutSession>();

        WorkoutProfileService profileService = new WorkoutProfileService();
        SetService setService = new SetService();

        List<WorkoutProfile> profiles = profileService.getAllProfiles();
        List<Set> sets = setService.getAllSets();
        Date now = new Date();
        Random rand = new Random();

        this.sessions.add(new WorkoutSession(now.getTime(), 100*(rand.nextFloat()+1), sets, profiles.get(0)));
    }

    public List<WorkoutSession> getAllSessions(){
        return new ArrayList<WorkoutSession>(sessions);
    }
}
