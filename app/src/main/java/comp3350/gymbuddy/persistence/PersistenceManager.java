package comp3350.gymbuddy.persistence;

import androidx.annotation.NonNull;

import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutSessionStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutStub;

public class PersistenceManager {
    private static IWorkoutDB workoutDB = null;
    private static IExerciseDB exerciseDB = null;
    private static IWorkoutSessionDB workoutSessionDB = null;

    @NonNull
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

    @NonNull
    public static synchronized IExerciseDB getExerciseDB(boolean forProduction) {
        if (exerciseDB == null) {
            if (forProduction) {
                exerciseDB = new ExerciseStub();
            } else {
                exerciseDB = new ExerciseHSQLDB();
            }
        }
        return exerciseDB;
    }

    @NonNull
    public static synchronized IWorkoutSessionDB getWorkoutSessionDB(boolean forProduction) {
        if (exerciseDB == null) {
            if (forProduction) {
                workoutSessionDB = new WorkoutSessionStub();
            } else {
                workoutSessionDB = new WorkoutSessionHSQLDB();
            }
        }
        return workoutSessionDB;
    }
}
