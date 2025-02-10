package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;

public class WorkoutProfileService {
    // these persistence classes are singletons - only 1 instance of them for the lifetime of the application
    // meaning there will only be 1 database connection the entire time
    private IWorkoutProfilePersistence profilePersistence;

    // Default constructor uses ExerciseStub
    public WorkoutProfileService() {
        if(this.profilePersistence != null){
            this.profilePersistence = new WorkoutProfileStub(); // Default stub implementation
        }
    }

    public WorkoutProfileService(IWorkoutProfilePersistence profilePersistence) {
        if(this.profilePersistence != null){
            this.profilePersistence = profilePersistence;
        }
    }

    public List<WorkoutProfile> getAllProfiles() {
        return this.profilePersistence.getAllWorkoutProfiles();
    }
}
