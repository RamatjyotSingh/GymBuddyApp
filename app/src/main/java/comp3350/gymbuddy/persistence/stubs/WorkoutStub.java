package comp3350.gymbuddy.persistence.stubs;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.util.WorkoutItemGenerator;

/**
 * WorkoutStub simulates a database of workout profiles for testing purposes.
 */
public class WorkoutStub implements IWorkoutDB {
    private static final int NUM_PROFILES = 4; // Number of generated profiles
    private final List<WorkoutProfile> profiles;
    private int nextId; // Simulated auto-incrementing ID

    /**
     * Initializes the stub with a set number of randomly generated workout profiles.
     */
    public WorkoutStub() {
        profiles = new ArrayList<>();
        nextId = 0;

        // Generate mock workout profiles.
        for (int i = 0; i < NUM_PROFILES; i++) {
            profiles.add(createWorkoutProfile());
        }
    }

    /**
     * Creates a new randomly generated workout profile.
     * @return A new WorkoutProfile object.
     */
    @NonNull
    private WorkoutProfile createWorkoutProfile() {
        int id = nextId;

        // Initialize a workout item generator for this profile.
        var workoutItemGenerator = new WorkoutItemGenerator(id);

        // Assign profile name and default icon.
        String name = "Profile " + id;
        String iconPath = null; // Use default icon.

        // Generate a list of workout items for the profile.
        List<WorkoutItem> workoutItems = workoutItemGenerator.generate();

        // Increment the ID counter for the next profile.
        nextId++;

        // Return the generated workout profile.
        return new WorkoutProfile(id, name, iconPath, workoutItems);
    }

    /**
     * Retrieves all workout profiles.
     * @return A list of workout profiles.
     */
    @Override
    public List<WorkoutProfile> getAll() {
        return Collections.unmodifiableList(profiles);
    }

    /**
     * Saves a new workout profile to the list.
     * @param profile The workout profile to save.
     */
    @Override
    public void saveWorkout(WorkoutProfile profile) {
        if (profile != null) {
            profiles.add(profile);
        }
    }

    /**
     * Retrieves a workout profile by its ID.
     * @param id The ID of the workout profile.
     * @return The matching workout profile, or null if not found.
     */
    @Override
    public WorkoutProfile getWorkoutProfileById(int id) {
        WorkoutProfile result = null;

        // Search for the matching profile ID.
        for (var profile : profiles) {
            if (profile.getId() == id) {
                result = profile;
                break;
            }
        }

        return result;
    }
}
