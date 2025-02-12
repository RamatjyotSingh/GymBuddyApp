package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;

import java.util.ArrayList;
import java.util.List;

public class WorkoutProfileStub implements IWorkoutProfilePersistence {
    private List<WorkoutProfile> profiles;

    public WorkoutProfileStub(){
        this.profiles = new ArrayList<WorkoutProfile>();

        AccessWorkoutItems itemService = new AccessWorkoutItems();
        List<WorkoutItem> items = itemService.getAllWorkoutItems();

        this.profiles.add(new WorkoutProfile("Profile 1", null, items));
    }

    public List<WorkoutProfile> getAllWorkoutProfiles(){
        return new ArrayList<WorkoutProfile>(this.profiles);
    }
}
