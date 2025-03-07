package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.util.WorkoutItemGenerator;

/**
 * WorkoutSessionStub simulates a database of workout sessions for testing purposes.
 */
public class WorkoutSessionStub implements IWorkoutSessionDB {

    // Constants for workout session generation.
    private static final int NUM_SESSIONS = 4;
    public static final int MIN_DURATION = 60; // Minimum session duration (1 min in seconds)
    public static final int MAX_DURATION = 2 * 60 * 60; // Maximum session duration (2 hrs in seconds)

    private final List<WorkoutSession> sessions;
    private int nextId; // Simulated auto-incrementing ID

    /**
     * Initializes the stub with a set number of randomly generated workout sessions.
     */
    public WorkoutSessionStub() {
        sessions = new ArrayList<>();
        nextId = 0;

        // Generate mock workout sessions.
        for (int i = 0; i < NUM_SESSIONS; i++) {
            sessions.add(createWorkoutSession());
        }
    }

    /**
     * Retrieves all workout sessions.
     * @return A list of workout sessions.
     */
    @Override
    public List<WorkoutSession> getAll() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Retrieves a workout session by its ID.
     * @param id The ID of the workout session.
     * @return The matching workout session, or null if not found.
     */
    @Override
    public WorkoutSession getWorkoutSessionByid(int id) {
        WorkoutSession result = null;

        // Search for the matching session ID.
        for (var session : sessions) {
            if (session.getId() == id) {
                result = session;
                break;
            }
        }

        return result;
    }

    /**
     * Creates a new randomly generated workout session.
     * @return A new WorkoutSession object.
     */
    private WorkoutSession createWorkoutSession() {
        int id = nextId;

        // Initialize a workout item generator for this session.
        var workoutItemGenerator = new WorkoutItemGenerator(id);

        // Retrieve a list of available workout profiles from the stub database.
        var workoutStub = new WorkoutStub();
        List<WorkoutProfile> profiles = workoutStub.getAll();

        // Generate a random session duration within the allowed range.
        Random rand = new Random();
        long duration = rand.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;

        // Get the current timestamp.
        Date now = new Date();

        // Determine session start and end times.
        long startTime = now.getTime();
        long endTime = now.getTime() + duration * 1000; // Convert seconds to milliseconds

        // Generate a list of workout items for the session.
        List<WorkoutItem> workoutItems = workoutItemGenerator.generate();

        // Assign a random workout profile to the session.
        WorkoutProfile profile = profiles.get(rand.nextInt(profiles.size()));

        // Increment the ID counter for the next session.
        nextId++;

        // Return the generated workout session.
        return new WorkoutSession(id, startTime, endTime, workoutItems, profile);
    }
}
