package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;

public class AccessWorkoutProfiles {
    private IWorkoutProfilePersistence profilePersistence;

    public AccessWorkoutProfiles() {
        profilePersistence = Services.getWorkoutProfilePersistence();
    }

    public List<WorkoutProfile> getAllProfiles() {
        return Collections.unmodifiableList(profilePersistence.getAllWorkoutProfiles());
    }

    public void addProfile(WorkoutProfile profile) {
        profilePersistence.addWorkoutProfile(profile);
    }
}
