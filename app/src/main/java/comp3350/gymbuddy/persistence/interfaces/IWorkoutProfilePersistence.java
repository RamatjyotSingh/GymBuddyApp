package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;

public interface IWorkoutProfilePersistence extends IPersistence{
    List<WorkoutProfile> getAll();
}
