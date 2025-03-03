package comp3350.gymbuddy.application;

import android.content.Context;
import java.sql.Connection;

import comp3350.gymbuddy.persistence.database.DatabaseManager;
import comp3350.gymbuddy.persistence.interfaces.*;
import comp3350.gymbuddy.persistence.hsqldb.*;
import comp3350.gymbuddy.persistence.stubs.*;

public class Services {
    private static IExercisePersistence exercisePersistence = null;
    private static ITagPersistence tagPersistence = null;
    private static ISessionItemPersistence sessionItemPersistence = null;
    private static IWorkoutItemPersistence workoutItemPersistence = null;
    private static IWorkoutProfilePersistence workoutProfilePersistence = null;
    private static IWorkoutSessionPersistence workoutSessionPersistence = null;
    private static DatabaseManager dbManager = null;

    // set false to use stub
    private static final boolean hsqlDataBase = true;

    // Initialize database with application context
    public static void initDatabase(Context context) {
        if (hsqlDataBase && dbManager == null) {
            dbManager = DatabaseManager.getInstance(context);
        }
    }

    // Get database connection
    private static Connection getConnection() {
        return (dbManager != null) ? dbManager.getConnection() : null;
    }

    public static synchronized IExercisePersistence getExercisePersistence() {
        if (exercisePersistence == null) {
            if (hsqlDataBase && dbManager != null) {
                Connection connection = getConnection();
                exercisePersistence = new ExerciseHSQLDB(connection);
            } else {
                exercisePersistence = new ExerciseStub();
            }
        }
        return exercisePersistence;
    }

    public static synchronized ITagPersistence getTagPersistence() {
        if (tagPersistence == null) {
            if (hsqlDataBase && dbManager != null) {
                Connection connection = getConnection();
                tagPersistence = new TagHSQLDB(connection);
            } else {
                tagPersistence = new TagStub();
            }
        }
        return tagPersistence;
    }

    public static synchronized ISessionItemPersistence getSessionItemPersistence() {
        if (sessionItemPersistence == null) {
            // Add HSQLDB implementation when ready
            sessionItemPersistence = new SessionItemStub();
        }
        return sessionItemPersistence;
    }

    public static synchronized IWorkoutItemPersistence getWorkoutItemPersistence() {
        if (workoutItemPersistence == null) {
            // Add HSQLDB implementation when ready
            workoutItemPersistence = new WorkoutItemStub();
        }
        return workoutItemPersistence;
    }

    public static synchronized IWorkoutProfilePersistence getWorkoutProfilePersistence() {
        if (workoutProfilePersistence == null) {
            // Add HSQLDB implementation when ready
            workoutProfilePersistence = new WorkoutProfileStub();
        }
        return workoutProfilePersistence;
    }

    public static synchronized IWorkoutSessionPersistence getWorkoutSessionPersistence() {
        if (workoutSessionPersistence == null) {
            // Add HSQLDB implementation when ready
            workoutSessionPersistence = new WorkoutSessionStub();
        }
        return workoutSessionPersistence;
    }

    // Clean up resources
    public static void closeDatabase() {
        if (dbManager != null) {
            dbManager.closeConnection();
            dbManager = null;
        }
    }
}