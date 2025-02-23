package comp3350.gymbuddy.persistence;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;

public interface IWorkoutProfilePersistence {
    List<WorkoutProfile> getAllWorkoutProfiles();
}
