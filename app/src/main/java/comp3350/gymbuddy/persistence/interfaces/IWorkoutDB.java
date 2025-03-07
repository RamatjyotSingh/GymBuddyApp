package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;

public interface IWorkoutDB {
    List<WorkoutProfile> getAll() throws DBException;

    void saveWorkout(WorkoutProfile profile) throws DBException;

    WorkoutProfile getWorkoutProfileById(int id) throws DBException;
}
