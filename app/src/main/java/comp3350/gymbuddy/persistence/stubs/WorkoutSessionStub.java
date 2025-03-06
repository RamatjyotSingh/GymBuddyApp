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

public class WorkoutSessionStub implements IWorkoutSessionDB {

    // Constants for workout session generation.
    private static final int NUM_SESSIONS = 4;
    public static final int MIN_DURATION = 60; // 1 min
    public static final int MAX_DURATION = 2 * 60 * 60; // 2hrs in sec

    private final List<WorkoutSession> sessions;
    private int nextId; // Simulated auto-incrementing ID

    public WorkoutSessionStub() {
        // Initialize workout session list.
        sessions = new ArrayList<>();
        nextId = 0;

        for (int i = 0; i < NUM_SESSIONS; i++) {
            sessions.add(createWorkoutSession());
        }
    }

    @Override
    public List<WorkoutSession> getAll() {
        return Collections.unmodifiableList(sessions);
    }

    @Override
    public WorkoutSession getWorkoutSessionByid(int id) {
        WorkoutSession result = null;

        // Search for the ID in the list of sessions.
        for (var session : sessions) {
            if (session.getId() == id) {
                result = session;
                break;
            }
        }

        return result;
    }

    private WorkoutSession createWorkoutSession() {
        int id = nextId;

        // Get a workout item generator.
        var workoutItemGenerator = new WorkoutItemGenerator(id);

        // Get some mock data for profiles.
        var workoutStub = new WorkoutStub();
        List<WorkoutProfile> profiles = workoutStub.getAll();

        // Generate random duration.
        Random rand = new Random();
        long duration = rand.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;

        // Get current time.
        Date now = new Date();

        // Determine the profile information.
        long startTime = now.getTime();
        long endTime = now.getTime() + duration;
        List<WorkoutItem> workoutItems = workoutItemGenerator.generate();
        WorkoutProfile profile = profiles.get(rand.nextInt(profiles.size()));

        // Increment the ID counter.
        nextId++;

        // Return the new profile.
        return new WorkoutSession(id, startTime, endTime, workoutItems, profile);
    }
}
