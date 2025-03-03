package comp3350.gymbuddy.persistence.interfaces;

import comp3350.gymbuddy.objects.WorkoutSession;

import java.util.List;

public interface IWorkoutSessionPersistence extends IPersistence{
    List<WorkoutSession> getAll();
    WorkoutSession getByStartTime(long startTime);
}
