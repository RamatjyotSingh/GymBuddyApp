package comp3350.gymbuddy.persistence.factory;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDatabase;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutHSQLDB;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionHSQLDB;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;

import java.sql.Connection;

/**
 * Factory for creating HSQLDB implementations
 */
public class HSQLDBFactory implements DatabaseFactory {
    private final String scriptPath;
    private final String configPath;
    private HSQLDatabase database;
    
    public HSQLDBFactory(String scriptPath, String configPath) {
        this.scriptPath = scriptPath;
        this.configPath = configPath;
    }
    
    @Override
    public IDatabase createDatabase() throws DBException {
        if (database == null) {
            database = new HSQLDatabase();
            database.setup(scriptPath, configPath);
        }
        return database;
    }
    
    @Override
    public IWorkoutDB createWorkoutDB() {
        try {
            Connection connection = getConnection();
            return new WorkoutHSQLDB(connection);
        } catch (DBException e) {
            throw new RuntimeException("Failed to create WorkoutHSQLDB", e);
        }
    }
    
    @Override
    public IExerciseDB createExerciseDB() {
        try {
            Connection connection = getConnection();
            return new ExerciseHSQLDB(connection);
        } catch (DBException e) {
            throw new RuntimeException("Failed to create ExerciseHSQLDB", e);
        }
    }
    
    @Override
    public IWorkoutSessionDB createWorkoutSessionDB() {
        try {
            Connection connection = getConnection();
            return new WorkoutSessionHSQLDB(connection);
        } catch (DBException e) {
            throw new RuntimeException("Failed to create WorkoutSessionHSQLDB", e);
        }
    }
    
    private Connection getConnection() throws DBException {
        if (database == null) {
            createDatabase();
        }
        return database.getConnection();
    }
}