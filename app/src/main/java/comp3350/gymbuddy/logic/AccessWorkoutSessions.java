package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public class AccessWorkoutSessions extends Access{
    private IWorkoutSessionPersistence workoutSessionPersistence;

    public AccessWorkoutSessions(){
        workoutSessionPersistence = Services.getWorkoutSessionPersistence();
    }

    public AccessWorkoutSessions(IWorkoutSessionPersistence workoutSessionPersistence){
        this();
        this.workoutSessionPersistence = workoutSessionPersistence;
    }

    public List<WorkoutSession> getAll(){
        return this.workoutSessionPersistence.getAll();
    }

    public void insertWorkoutSession(WorkoutSession session) {
    if (this.workoutSessionPersistence != null && session != null) {
        this.workoutSessionPersistence.insertWorkoutSession(session);
    }
}
}
