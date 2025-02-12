package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.objects.WorkoutProfile;

import java.util.List;

public interface IWorkoutProfilePersistence {
    List<WorkoutProfile> getAllWorkoutProfiles();
    void addWorkoutProfile(WorkoutProfile profile);
}
