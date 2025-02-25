package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;


public class WorkoutProfileStub implements IWorkoutProfilePersistence {
    private final List<WorkoutProfile> profiles;

    public WorkoutProfileStub(){
        profiles = new ArrayList<>();

        profiles.add(new WorkoutProfile("Profile 1", null, Services.getWorkoutItemPersistence().getAll()));
    }

    public List<WorkoutProfile> getAll(){
        return Collections.unmodifiableList(this.profiles);
    }
}
