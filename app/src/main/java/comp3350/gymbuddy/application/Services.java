package comp3350.gymbuddy.application;

import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;
import comp3350.gymbuddy.persistence.interfaces.ITagPersistence;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import comp3350.gymbuddy.persistence.stubs.TagStub;
import comp3350.gymbuddy.persistence.stubs.SessionItemStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutItemStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutSessionStub;

public class Services {
    private static IExercisePersistence exercisePersistence = null;
    private static ITagPersistence tagPersistence = null;
    private static ISessionItemPersistence sessionItemPersistence = null;
    private static IWorkoutItemPersistence workoutItemPersistence = null;
    private static IWorkoutProfilePersistence workoutProfilePersistence = null;
    private static IWorkoutSessionPersistence workoutSessionPersistence = null;

    public static synchronized IExercisePersistence getExercisePersistence() {
        if (exercisePersistence == null) {
            exercisePersistence = new ExerciseStub(); // Stub version for now
        }
        return exercisePersistence;
    }

    public static synchronized ITagPersistence getTagPersistence() {
        if (tagPersistence == null) {
            tagPersistence = new TagStub(); // Stub version for now
        }
        return tagPersistence;
    }

    public static synchronized ISessionItemPersistence getSessionItemPersistence() {
        if (sessionItemPersistence == null) {
            sessionItemPersistence = new SessionItemStub(); // Stub version for now
        }
        return sessionItemPersistence;
    }

    public static synchronized IWorkoutItemPersistence getWorkoutItemPersistence() {
        if (workoutItemPersistence == null) {
            workoutItemPersistence = new WorkoutItemStub(); // Stub version for now
        }
        return workoutItemPersistence;
    }

    public static synchronized IWorkoutProfilePersistence getWorkoutProfilePersistence() {
        if (workoutProfilePersistence == null) {
            workoutProfilePersistence = new WorkoutProfileStub(); // Stub version for now
        }
        return workoutProfilePersistence;
    }

    public static synchronized IWorkoutSessionPersistence getWorkoutSessionPersistence() {
        if (workoutSessionPersistence == null) {
            workoutSessionPersistence = new WorkoutSessionStub(); // Stub version for now
        }
        return workoutSessionPersistence;
    }
}
