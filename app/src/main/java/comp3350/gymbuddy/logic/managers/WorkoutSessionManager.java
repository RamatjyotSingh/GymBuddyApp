package comp3350.gymbuddy.logic.managers;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

public class WorkoutSessionManager {
    private final IWorkoutSessionDB workoutSessionDB;

    public WorkoutSessionManager(boolean forProduction) {
        workoutSessionDB = PersistenceManager.getWorkoutSessionDB(forProduction);
    }

    public List<WorkoutSession> getAll() {
        return Collections.unmodifiableList(workoutSessionDB.getAll());
    }

    public WorkoutSession getWorkoutSessionById(int id) {
        return workoutSessionDB.getWorkoutSessionByid(id);
    }
}
