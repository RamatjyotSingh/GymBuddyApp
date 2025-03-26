package comp3350.gymbuddy.logic.managers;

import java.util.ArrayList;
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

    public WorkoutSession getWorkoutSessionByID(int id) {
        return workoutSessionDB.getWorkoutSessionByid(id);
    }

    public List<WorkoutSession> search(String searchString){
        List<WorkoutSession> results = new ArrayList<>();

        if (searchString != null && !searchString.isEmpty()) {
            for (var session : workoutSessionDB.getAll()) {
                if (session.getDate().toLowerCase().contains(searchString.toLowerCase())) {
                    results.add(session);
                }
                else if(session.getWorkoutProfile().getName().toLowerCase().contains(searchString.toLowerCase())){
                    results.add(session);
                }
            }
        }

        return results;
    }
}
