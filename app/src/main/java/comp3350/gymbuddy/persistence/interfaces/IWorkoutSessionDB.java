package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;

public interface IWorkoutSessionDB {
    // Read operations
    List<WorkoutSession> getAll() throws DBException;
    WorkoutSession getWorkoutSessionByid(int id) throws DBException;
    List<WorkoutSession> search(String query) throws DBException;
    List<WorkoutItem> getExercisesForSession(int sessionId) throws DBException;
    
    // Create operations
    boolean insertSession(WorkoutSession session) throws DBException;
    boolean addExerciseToSession(int sessionId, WorkoutItem item) throws DBException;
    
    // Update operations
    boolean updateSession(WorkoutSession session) throws DBException;
    boolean updateSessionEndTime(int sessionId, long endTime) throws DBException;
    
    // Delete operations
    boolean deleteSession(int sessionId) throws DBException;
    boolean removeExerciseFromSession(int sessionId, int exerciseId) throws DBException;
}
