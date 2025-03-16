package comp3350.gymbuddy.logic.managers;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutManager  {
    private final IWorkoutDB workoutProfileDB;

    public WorkoutManager(boolean forProduction) {
        workoutProfileDB = PersistenceManager.getWorkoutDB(forProduction);
    }

    public List<WorkoutProfile> getAll() {
        return Collections.unmodifiableList(workoutProfileDB.getAll());
    }

    public boolean saveWorkout(WorkoutProfile workoutProfile) {
       return workoutProfileDB.saveWorkout(workoutProfile);
    }
}
