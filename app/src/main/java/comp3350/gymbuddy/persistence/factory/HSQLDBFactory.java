package comp3350.gymbuddy.persistence.factory;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.ExerciseDAO;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDatabase;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutDAO;
import comp3350.gymbuddy.persistence.hsqldb.WorkoutSessionDAO;
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
    private static final String TAG = "HSQLDBFactory";

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
    public IWorkoutDB createWorkoutDB() throws DBException {
       
            Connection conn = database.getConnection();
            IExerciseDB exerciseDB = createExerciseDB();
            return new WorkoutDAO(conn, exerciseDB);
        
    }

    @Override
    public IExerciseDB createExerciseDB() throws DBException {
       
            return new ExerciseDAO(database.getConnection());
       
    }

    @Override
    public IWorkoutSessionDB createWorkoutSessionDB() throws DBException {
       
            Connection conn = database.getConnection();
            IExerciseDB exerciseDB = createExerciseDB();
            IWorkoutDB workoutDB = new WorkoutDAO(conn, exerciseDB);
            return new WorkoutSessionDAO(conn, workoutDB, exerciseDB);
       
    }


}