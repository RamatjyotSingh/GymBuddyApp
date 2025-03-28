package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.logic.exception.ApplicationInitException;
import comp3350.gymbuddy.logic.exception.DataAccessException;
import comp3350.gymbuddy.logic.managers.ExerciseManager;
import comp3350.gymbuddy.logic.managers.WorkoutManager;
import comp3350.gymbuddy.logic.managers.WorkoutSessionManager;
import comp3350.gymbuddy.logic.util.ConfigLoader;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import timber.log.Timber;

/**
 * Central service that manages application initialization and provides access to business managers
 * Implemented as a singleton for easy access from any activity
 */
public class ApplicationService {
    private static final String TAG = "ApplicationService";
    
    // Singleton instance
    private static ApplicationService instance;

    // Managers
    private ExerciseManager exerciseManager;
    private WorkoutManager workoutManager;
    private WorkoutSessionManager workoutSessionManager;
    
    // State tracking
    private volatile boolean initialized = false;
    private volatile boolean closed = false;
    
    /**
     * Private constructor for singleton pattern
     */
    private ApplicationService() {}
    
    /**
     * Get the singleton instance
     * @return ApplicationService instance
     */
    public static synchronized ApplicationService getInstance() {
        if (instance == null) {
            instance = new ApplicationService();
        }
        return instance;
    }
    
    /**
     * Initializes the application services including database and managers
     * @param config Configuration for database initialization
     * @throws ApplicationInitException if initialization fails
     */
    public synchronized void initialize(ConfigLoader config) throws ApplicationInitException {
        if (initialized && !closed) {
            Timber.tag(TAG).w("Application already initialized");
            return;
        }

        // Reset closed state if we're reinitializing
        if (closed) {
            Timber.tag(TAG).i("Reinitializing previously closed application");
            closed = false;
        }

        // Initialize database
        PersistenceManager pm = PersistenceManager.getInstance();
        pm.initialize(
            config.getScriptPath(),
            config.getConfigPath(),
            config.isTestMode(),
            config.isDbAlreadyExists()
        );
        
        // Initialize managers
        initializeManagers();
        
        initialized = true;
        Timber.tag(TAG).i("Application initialized successfully");
    }
    
    /**
     * Creates all manager instances using database implementations
     */
    private void initializeManagers() {
        PersistenceManager pm = PersistenceManager.getInstance();
        
        // Get database implementations
        IExerciseDB exerciseDB = pm.getExerciseDB();
        IWorkoutDB workoutDB = pm.getWorkoutDB();
        IWorkoutSessionDB workoutSessionDB = pm.getWorkoutSessionDB();
        
        // Create managers
        exerciseManager = new ExerciseManager(exerciseDB);
        workoutManager = new WorkoutManager(workoutDB);
        workoutSessionManager = new WorkoutSessionManager(workoutSessionDB);
        
        Timber.tag(TAG).d("Managers initialized");
    }
    
    /**
     * @return ExerciseManager instance
     * @throws ExerciseAccessException if application is not initialized or has been closed
     */
    public ExerciseManager getExerciseManager() {
        ensureActive();
        return exerciseManager;
    }
    
    /**
     * @return WorkoutManager instance
     * @throws IllegalStateException if application is not initialized or has been closed
     */
    public WorkoutManager getWorkoutManager() {
        ensureActive();
        return workoutManager;
    }
    
    /**
     * @return WorkoutSessionManager instance
     * @throws  WorkoutSessionAccessException if application is not initialized or has been closed
     */
    public WorkoutSessionManager getWorkoutSessionManager() {
        ensureActive();
        return workoutSessionManager;
    }
    
    /**
     * Releases all resources and closes the application
     */

    public synchronized void close() {
        if (closed) {
            Timber.tag(TAG).d("Application already closed");
            return;
        }
        
        Timber.tag(TAG).i("Shutting down application...");
        
        // Reset managers first
        exerciseManager = null;
        workoutManager = null;
        workoutSessionManager = null;
        
        // Close persistence
        try {
            PersistenceManager.getInstance().close();
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Error during application shutdown");
        }
        
        initialized = false;
        closed = true;
        Timber.tag(TAG).i("Application shutdown complete");
    }
    
    /**
     * Ensures the application service is properly initialized and not closed
     * @throws ApplicationInitException if service is not initialized or has been closed
     */
    private void ensureActive() {
        if (!initialized) {
            throw new ApplicationInitException("Application service is not initialized");
        }
        if (closed) {
            throw new ApplicationInitException("Application service has been closed");
        }
    }
}
