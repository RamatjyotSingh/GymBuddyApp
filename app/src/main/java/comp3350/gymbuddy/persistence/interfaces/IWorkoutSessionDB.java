package comp3350.gymbuddy.persistence.interfaces;

import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;

import java.util.List;

public interface IWorkoutSessionDB {
    List<WorkoutSession> getAll() throws DBException;

    WorkoutSession getWorkoutSessionByid(int id) throws DBException;
}
