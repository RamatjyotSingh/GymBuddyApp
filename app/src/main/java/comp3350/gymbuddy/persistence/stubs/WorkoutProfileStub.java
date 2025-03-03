package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;

public class WorkoutProfileStub implements IWorkoutProfilePersistence {
    private final List<WorkoutProfile> profiles;
    private int nextId; // Simulated auto-incrementing ID

    public WorkoutProfileStub() {
        profiles = new ArrayList<>();
        nextId = 1; // Start ID count

        AccessWorkoutItems accessWorkoutItems = new AccessWorkoutItems();

        // Assign unique IDs while adding multiple profiles
        profiles.add(new WorkoutProfile(nextId++, "Profile 1", null, accessWorkoutItems.getAll()));
        profiles.add(new WorkoutProfile(nextId++, "Profile 2", null, accessWorkoutItems.getAll()));
        profiles.add(new WorkoutProfile(nextId++, "Profile 3", null, accessWorkoutItems.getAll()));
        profiles.add(new WorkoutProfile(nextId++, "Profile 4", null, accessWorkoutItems.getAll()));
    }

    @Override
    public List<WorkoutProfile> getAll() {
        return Collections.unmodifiableList(this.profiles);
    }

    @Override
    public void insertWorkoutProfile(WorkoutProfile profile) {
        if (profile != null) {
            profile.setId(nextId++); // Assign a simulated unique ID
            profiles.add(profile);
        }
    }

    @Override
    public WorkoutProfile getWorkoutProfileById(int profileId) {
        for (WorkoutProfile profile : profiles) {
            if (profile.getId() == profileId) {
                return profile;
            }
        }
        return null; // If not found, return null
    }
}
