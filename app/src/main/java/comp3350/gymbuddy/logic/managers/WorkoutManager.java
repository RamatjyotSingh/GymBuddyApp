package comp3350.gymbuddy.logic.managers;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutManager  {
    private final IWorkoutDB workoutDB;

    public WorkoutManager(boolean forProduction) {
        workoutDB = PersistenceManager.getWorkoutDB(forProduction);
    }

    public List<WorkoutProfile> getAll() {
        return Collections.unmodifiableList(workoutDB.getAll());
    }

    public boolean saveWorkout(WorkoutProfile workoutProfile) {
       return workoutDB.saveWorkout(workoutProfile);
    }

    public WorkoutProfile getWorkoutProfileByID(int id) {
        return workoutDB.getWorkoutProfileById(id);
    }

    public void deleteWorkout(int id) {
        workoutDB.deleteWorkout(id);
    }
}
