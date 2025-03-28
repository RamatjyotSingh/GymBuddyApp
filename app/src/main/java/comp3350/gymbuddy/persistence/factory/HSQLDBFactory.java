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
import timber.log.Timber;

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
            return new WorkoutHSQLDB(conn, exerciseDB);
        
    }

    @Override
    public IExerciseDB createExerciseDB() throws DBException {
       
            return new ExerciseHSQLDB(database.getConnection());
       
    }

    @Override
    public IWorkoutSessionDB createWorkoutSessionDB() throws DBException {
       
            Connection conn = database.getConnection();
            IExerciseDB exerciseDB = createExerciseDB();
            IWorkoutDB workoutDB = new WorkoutHSQLDB(conn, exerciseDB);
            return new WorkoutSessionHSQLDB(conn, workoutDB, exerciseDB);
       
    }


}