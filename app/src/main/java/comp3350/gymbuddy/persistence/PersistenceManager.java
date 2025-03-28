package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.logic.exception.DataAccessException;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.factory.DatabaseFactory;
import comp3350.gymbuddy.persistence.factory.HSQLDBFactory;
import comp3350.gymbuddy.persistence.factory.StubDatabaseFactory;
import comp3350.gymbuddy.persistence.interfaces.IDatabase;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import timber.log.Timber;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * PersistenceManager handles database initialization and access for the application.
 * This class follows a singleton pattern for consistent database access.
 */

public class PersistenceManager implements AutoCloseable {
    private static final String TAG = "PersistenceManager";
    private static final PersistenceManager instance = new PersistenceManager();
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    // Database repositories
    private IWorkoutDB workoutDB;
    private IExerciseDB exerciseDB;
    private IWorkoutSessionDB workoutSessionDB;
    
    // Database and factory
    private IDatabase database;
    private DatabaseFactory databaseFactory;
    
    // Configuration state
    private String scriptPath;
    private String configPath;
    private boolean isInitialized;
    
    /**
     * Private constructor for singleton pattern
     */
    private PersistenceManager() {}
    
    /**
     * Get the singleton instance
     * @return PersistenceManager instance
     */
    public static PersistenceManager getInstance() {
        return instance;
    }
    
    /**
     * Initialize the database system with provided paths
     * @param scriptPath Path to SQL initialization script
     * @param configPath Path to database configuration file
     * @param useTestMode Whether to use test data instead of real database
     * @throws DataAccessException If database initialization fails
     */
    public synchronized void initialize(String scriptPath, String configPath, boolean useTestMode,boolean dbAlreadyExists) 
            throws DataAccessException {
        if (isInitialized) {
            Timber.tag(TAG).w("Database already initialized, skipping");
            return;
        }
        
        this.scriptPath = scriptPath;
        this.configPath = configPath;
        
        // Reset any existing instances
        reset();
        
        // Create appropriate database factory
        databaseFactory = useTestMode ? 
            new StubDatabaseFactory() : 
            new HSQLDBFactory(scriptPath, configPath);
        
        // Initialize database if not using stubs
        if (!useTestMode) {
            try {
                database = databaseFactory.createDatabase();
                if(!dbAlreadyExists) {
                    database.initialize();
                }
            } catch (DBException e) {
                Timber.tag(TAG).e(e, "Failed to initialize database");
                throw new DataAccessException("Failed to initialize database", e);
            }
        }
        
        isInitialized = true;
        Timber.tag(TAG).i("Database initialized successfully. Test mode: %s", useTestMode);
    }
    
    /**
     * Check if the persistence system has been initialized
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return isInitialized; // Reading a boolean is atomic
    }
    
    /**
     * Retrieves an instance of IWorkoutDB
     * @return An instance of IWorkoutDB
     * @throws IllegalStateException if persistence system is not initialized
     */
    public synchronized IWorkoutDB getWorkoutDB() {
        ensureInitialized();
        
        if (workoutDB == null) {
            workoutDB = databaseFactory.createWorkoutDB();
            Timber.tag(TAG).d("Created WorkoutDB implementation");
        }
        return workoutDB;
    }

    /**
     * Retrieves an instance of IExerciseDB
     * @return An instance of IExerciseDB
     * @throws IllegalStateException if persistence system is not initialized
     */
    public synchronized IExerciseDB getExerciseDB() {
        ensureInitialized();
        
        if (exerciseDB == null) {
            exerciseDB = databaseFactory.createExerciseDB();
            Timber.tag(TAG).d("Created ExerciseDB implementation");
        }
        return exerciseDB;
    }

    /**
     * Retrieves an instance of IWorkoutSessionDB
     * @return An instance of IWorkoutSessionDB
     * @throws IllegalStateException if persistence system is not initialized
     */
    public synchronized IWorkoutSessionDB getWorkoutSessionDB() {
        ensureInitialized();
        
        if (workoutSessionDB == null) {
            workoutSessionDB = databaseFactory.createWorkoutSessionDB();
            Timber.tag(TAG).d("Created WorkoutSessionDB implementation");
        }
        return workoutSessionDB;
    }
    
    /**
     * Creates a new IWorkoutDB instance (for avoiding circular dependencies)
     * @param useReal If true, uses real database implementation; otherwise uses stub
     * @return A new IWorkoutDB instance
     */
    public static IWorkoutDB getWorkoutDB(boolean useReal) {
        DatabaseFactory factory = useReal && instance.isInitialized ? 
            instance.databaseFactory : new StubDatabaseFactory();
        return factory.createWorkoutDB();
    }
    
    /**
     * Creates a new IExerciseDB instance (for avoiding circular dependencies)
     * @param useReal If true, uses real database implementation; otherwise uses stub
     * @return A new IExerciseDB instance
     */
    public static IExerciseDB getExerciseDB(boolean useReal) {
        DatabaseFactory factory = useReal && instance.isInitialized ? 
            instance.databaseFactory : new StubDatabaseFactory();
        return factory.createExerciseDB();
    }
    
    /**
     * Creates a new IWorkoutSessionDB instance (for avoiding circular dependencies)
     * @param useReal If true, uses real database implementation; otherwise uses stub
     * @return A new IWorkoutSessionDB instance
     */
    public static IWorkoutSessionDB getWorkoutSessionDB(boolean useReal) {
        DatabaseFactory factory = useReal && instance.isInitialized ? 
            instance.databaseFactory : new StubDatabaseFactory();
        return factory.createWorkoutSessionDB();
    }
    
    /**
     * Reset all database instances, forcing them to be recreated on next request
     */
    public synchronized void reset() {
        closeResources();
        
        workoutDB = null;
        exerciseDB = null;
        workoutSessionDB = null;
        database = null;
        
        Timber.tag(TAG).d("PersistenceManager reset completed");
    }
    
    /**
     * Shut down all database resources
     */
    public synchronized void shutdown() {
        closeResources();
        
        isInitialized = false;
        Timber.tag(TAG).d("PersistenceManager shutdown completed");
    }
    
    @Override
    public void close() {
        shutdown();
    }
    
    private void closeResources() {
        // Close each repository first
        closeQuietly(workoutDB);
        closeQuietly(exerciseDB);
        closeQuietly(workoutSessionDB);
        
        // Finally close the database
        if (database != null) {
            try {
                database.shutdown();
                Timber.tag(TAG).d("Database shutdown completed");
            } catch (DBException e) {
                Timber.tag(TAG).e(e, "Error during database shutdown");
            }
            database = null;
        }
    }
    
    private void closeQuietly(Object resource) {
        if (resource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) resource).close();
            } catch (Exception e) {
                Timber.tag(TAG).e(e, "Error closing resource");
            }
        }
    }
    
    private void ensureInitialized() {
        if (!isInitialized) {
            throw new IllegalStateException("Database has not been initialized. Call initialize() first.");
        }
    }
}
