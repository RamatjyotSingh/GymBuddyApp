package comp3350.gymbuddy.persistence.engines;

import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import comp3350.gymbuddy.persistence.stubs.SessionItemStub;
import comp3350.gymbuddy.persistence.stubs.TagStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutItemStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutSessionStub;

public class StubEngine implements IPersistenceEngine {
    private ExerciseStub exerciseStub = null;
    private SessionItemStub sessionItemStub = null;
    private TagStub tagStub = null;
    private WorkoutItemStub workoutItemStub = null;
    private WorkoutProfileStub workoutProfileStub = null;
    private WorkoutSessionStub workoutSessionStub = null;

    public ExerciseStub getExercisePersistence() {
        if (exerciseStub == null) {
            exerciseStub = new ExerciseStub();
        }
        return exerciseStub;
    }

    public TagStub getTagPersistence() {
        if (tagStub == null) {
            tagStub = new TagStub();
        }
        return tagStub;
    }

    public SessionItemStub getSessionItemPersistence() {
        if (sessionItemStub == null) {
            sessionItemStub = new SessionItemStub();
        }
        return sessionItemStub;
    }

    public WorkoutItemStub getWorkoutItemPersistence() {
        if (workoutItemStub == null) {
            workoutItemStub = new WorkoutItemStub();
        }
        return workoutItemStub;
    }

    public WorkoutProfileStub getWorkoutProfilePersistence() {
        if (workoutProfileStub == null) {
            workoutProfileStub = new WorkoutProfileStub();
        }
        return workoutProfileStub;
    }

    public WorkoutSessionStub getWorkoutSessionPersistence() {
        if (workoutSessionStub == null) {
            workoutSessionStub = new WorkoutSessionStub();
        }
        return workoutSessionStub;
    }
}
