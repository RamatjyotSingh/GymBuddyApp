package comp3350.gymbuddy.persistence.engines;

import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.ITagPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionPersistence;

public interface IPersistenceEngine {
    IExercisePersistence getExercisePersistence();
    ITagPersistence getTagPersistence();
    ISessionItemPersistence getSessionItemPersistence();
    IWorkoutItemPersistence getWorkoutItemPersistence();
    IWorkoutProfilePersistence getWorkoutProfilePersistence();
    IWorkoutSessionPersistence getWorkoutSessionPersistence();
}