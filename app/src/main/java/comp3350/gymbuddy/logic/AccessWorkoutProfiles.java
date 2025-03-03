package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;

public class AccessWorkoutProfiles extends Access{

    private IWorkoutProfilePersistence persistence;
    public AccessWorkoutProfiles(){
        this.persistence = Services.getWorkoutProfilePersistence();
    }

    public List<WorkoutProfile> getAll(){
        return Collections.unmodifiableList(this.persistence.getAll());
    }

    public AccessWorkoutProfiles(IWorkoutProfilePersistence workoutProfilePersistence){
        this();
        this.persistence = workoutProfilePersistence;
    }
    public void insertWorkoutProfile(WorkoutProfile workoutProfile){
        this.persistence.insertWorkoutProfile(workoutProfile);
    }
}
