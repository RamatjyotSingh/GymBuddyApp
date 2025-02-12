package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;

public class AccessWorkoutProfiles {
    // these persistence classes are singletons - only 1 instance of them for the lifetime of the application
    // meaning there will only be 1 database connection the entire time
    private IWorkoutProfilePersistence profilePersistence;

    // Default constructor uses ExerciseStub
    public AccessWorkoutProfiles() {
        if(this.profilePersistence != null){
            this.profilePersistence = new WorkoutProfileStub(); // Default stub implementation
        }
    }

    public AccessWorkoutProfiles(IWorkoutProfilePersistence profilePersistence) {
        if(this.profilePersistence != null){
            this.profilePersistence = profilePersistence;
        }
    }

    public List<WorkoutProfile> getAllProfiles() {
        return this.profilePersistence.getAllWorkoutProfiles();
    }

    public void addProfile(WorkoutProfile profile) {
        profilePersistence.addWorkoutProfile(profile);
    }
}
