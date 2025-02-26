package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;


public class WorkoutProfileStub implements IWorkoutProfilePersistence {
    private List<WorkoutProfile> profiles;

    public WorkoutProfileStub(){
        profiles = new ArrayList<>();

        AccessWorkoutItems accessWorkoutItems = new AccessWorkoutItems();
        profiles.add(new WorkoutProfile("Profile 1", null, accessWorkoutItems.getAll()));
    }

    public List<WorkoutProfile> getAll(){
        return Collections.unmodifiableList(this.profiles);
    }
}
