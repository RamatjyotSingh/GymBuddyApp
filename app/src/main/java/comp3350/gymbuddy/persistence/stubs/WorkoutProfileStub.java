package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutProfileStub implements IWorkoutProfilePersistence {
    private List<WorkoutProfile> profiles;

    public WorkoutProfileStub(){
        profiles = new ArrayList<>();

        profiles.add(new WorkoutProfile("Profile 1", null, null));
    }

    public List<WorkoutProfile> getAllWorkoutProfiles(){
        return Collections.unmodifiableList(this.profiles);
    }

    public void addWorkoutProfile(WorkoutProfile profile) {
        profiles.add(profile);
    }
}
