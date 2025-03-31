package comp3350.gymbuddy.persistence.factory;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

/**
 * Factory interface for creating database implementations
 */
public interface DatabaseFactory {
    /**
     * Creates a database instance
     * @return The database instance
     * @throws DBException if creation fails
     */
    IDatabase createDatabase() throws DBException;
    
    /**
     * Creates a workout database implementation
     * @return The IWorkoutDB implementation
     */
    IWorkoutDB createWorkoutDB() throws DBException;
    
    /**
     * Creates an exercise database implementation
     * @return The IExerciseDB implementation
     */
    IExerciseDB createExerciseDB() throws DBException;
    
    /**
     * Creates a workout session database implementation
     * @return The IWorkoutSessionDB implementation
     */
    IWorkoutSessionDB createWorkoutSessionDB() throws DBException;
}