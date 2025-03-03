package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public class AccessWorkoutSessions extends Access{
    public AccessWorkoutSessions(){
        persistence = Services.getWorkoutSessionPersistence();
    }

    public AccessWorkoutSessions(IWorkoutSessionPersistence workoutSessionPersistence){
        this();
        this.persistence = workoutSessionPersistence;
    }

    public List<WorkoutSession> getAll(){
        return this.persistence.getAll();
    }

    public WorkoutSession getByStartTime(long startTime) { return ((IWorkoutSessionPersistence)persistence).getByStartTime(startTime); }
}
