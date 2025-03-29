package comp3350.gymbuddy.persistence.factory;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutSessionStub;
import comp3350.gymbuddy.persistence.stubs.WorkoutStub;

/**
 * Factory for creating stub database implementations for testing
 */
public class StubDatabaseFactory implements DatabaseFactory {
    
    @Override
    public IDatabase createDatabase() {
        return null; // Stubs don't need a real database
    }
    
    @Override
    public IWorkoutDB createWorkoutDB() {
        return new WorkoutStub();
    }
    
    @Override
    public IExerciseDB createExerciseDB() {
        return new ExerciseStub();
    }
    
    @Override
    public IWorkoutSessionDB createWorkoutSessionDB() {
        return new WorkoutSessionStub();
    }
}