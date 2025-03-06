package comp3350.gymbuddy.persistence.stubs;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.util.WorkoutItemGenerator;

public class WorkoutStub implements IWorkoutDB {
    private static final int NUM_PROFILES = 4;
    private final List<WorkoutProfile> profiles;
    private int nextId; // Simulated auto-incrementing ID

    public WorkoutStub() {
        // Initialize workout item list.
        profiles = new ArrayList<>();
        nextId = 0;

        for (int i = 0; i < NUM_PROFILES; i++) {
            profiles.add(createWorkoutProfile());
        }
    }

    @NonNull
    private WorkoutProfile createWorkoutProfile() {
        int id = nextId;

        // Get a workout item generator.
        var workoutItemGenerator = new WorkoutItemGenerator(id);

        // Determine the profile information.
        String name = "Profile " + id;
        String iconPath = null; // Use default icon.
        List<WorkoutItem> workoutItems = workoutItemGenerator.generate();

        // Increment the ID counter.
        nextId++;

        // Return the new profile.
        return new WorkoutProfile(id, name, iconPath, workoutItems);
    }

    @Override
    public List<WorkoutProfile> getAll() {
        return Collections.unmodifiableList(profiles);
    }

    @Override
    public void saveWorkout(WorkoutProfile profile) {
        if (profile != null) {
            profiles.add(profile);
        }
    }

    @Override
    public WorkoutProfile getWorkoutProfileById(int id) {
        WorkoutProfile result = null;

        // Search for the ID in the list of profiles.
        for (var profile : profiles) {
            if (profile.getId() == id) {
                result = profile;
                break;
            }
        }

        return result;
    }
}
