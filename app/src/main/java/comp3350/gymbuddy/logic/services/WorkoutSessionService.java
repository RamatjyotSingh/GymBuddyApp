package comp3350.gymbuddy.logic.services;

import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public class WorkoutSessionService {
    private IWorkoutSessionPersistence persistence;

    public WorkoutSessionService() {
        persistence = Services.getWorkoutSessionPersistence();
    }

    public WorkoutSessionService(IWorkoutSessionPersistence workoutSessionPersistence) {
        persistence = workoutSessionPersistence;
    }

    public List<WorkoutSession> getAll() {
        return persistence.getAll();
    }

    public WorkoutSession getByStartTime(long startTime) {
        return persistence.getByStartTime(startTime);
    }

    public void insertWorkoutSession(WorkoutSession session) {
        persistence.insertWorkoutSession(session);
    }
}
