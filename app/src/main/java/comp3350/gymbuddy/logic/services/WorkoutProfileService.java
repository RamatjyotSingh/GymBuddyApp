package comp3350.gymbuddy.logic.services;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;

public class WorkoutProfileService {

    private IWorkoutProfilePersistence persistence;
    public WorkoutProfileService() {
        this.persistence = Services.getWorkoutProfilePersistence();
    }

    public WorkoutProfileService(IWorkoutProfilePersistence workoutProfilePersistence) {
        persistence = workoutProfilePersistence;
    }

    public List<WorkoutProfile> getAll(){
        return Collections.unmodifiableList(this.persistence.getAll());
    }

    public void insertWorkoutProfile(WorkoutProfile workoutProfile){
        this.persistence.insertWorkoutProfile(workoutProfile);
    }
}
