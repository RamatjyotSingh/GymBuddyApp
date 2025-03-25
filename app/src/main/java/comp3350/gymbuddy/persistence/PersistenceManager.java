package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutSessionStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutStub;

/**
 * PersistenceManager handles the initialization and retrieval of database instances.
 * It provides access to either the production database (HSQLDB) or stub data for testing.
 */
public class PersistenceManager {
    private static IWorkoutDB workoutDB = null;
    private static IExerciseDB exerciseDB = null;
    private static IWorkoutSessionDB workoutSessionDB = null;

    /**
     * Retrieves an instance of IWorkoutDB.
     * If the instance does not exist, it is created based on the environment (production or testing).
     * @param forProduction If true, returns an HSQLDB instance; otherwise, returns a stub.
     * @return An instance of IWorkoutDB.
     */
    public static synchronized IWorkoutDB getWorkoutDB(boolean forProduction) {
        if (workoutDB == null) {
            if (forProduction) {
                workoutDB = new WorkoutHSQLDB();
            } else {
                workoutDB = new WorkoutStub();
            }
        }
        return workoutDB;
    }

    /**
     * Retrieves an instance of IExerciseDB.
     * If the instance does not exist, it is created based on the environment (production or testing).
     * @param forProduction If true, returns an HSQLDB instance; otherwise, returns a stub.
     * @return An instance of IExerciseDB.
     */
    public static synchronized IExerciseDB getExerciseDB(boolean forProduction) {
        if (exerciseDB == null) {
            if (forProduction) {
                exerciseDB = new ExerciseHSQLDB();
            } else {
                exerciseDB = new ExerciseStub();
            }
        }
        return exerciseDB;
    }

    /**
     * Retrieves an instance of IWorkoutSessionDB.
     * If the instance does not exist, it is created based on the environment (production or testing).
     * @param forProduction If true, returns an HSQLDB instance; otherwise, returns a stub.
     * @return An instance of IWorkoutSessionDB.
     */
    public static synchronized IWorkoutSessionDB getWorkoutSessionDB(boolean forProduction) {
        if (workoutSessionDB == null) {
            if (forProduction) {
                workoutSessionDB = new WorkoutSessionHSQLDB();
            } else {
                workoutSessionDB = new WorkoutSessionStub();
            }
        }
        return workoutSessionDB;
    }
}
