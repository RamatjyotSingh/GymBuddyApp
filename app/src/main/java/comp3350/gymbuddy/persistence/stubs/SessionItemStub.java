package comp3350.gymbuddy.persistence.stubs;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;

public class SessionItemStub implements ISessionItemPersistence {
    private static final int MIN_REPS = 1;
    private static final int MAX_REPS = 18;
    private static final double MIN_TIME = 5.0;
    private static final double MAX_TIME = 60.0;
    private final List<SessionItem> sessionItems;
    private final Map<Integer, List<SessionItem>> sessionIdMap;
    private int nextId; // Simulated session item ID counter


    public SessionItemStub() {
        sessionItems = new ArrayList<>();
        sessionIdMap = new HashMap<>();
        nextId = 1; // Start ID counter

        AccessWorkoutItems accessWorkoutItems = new AccessWorkoutItems();
        List<WorkoutItem> items = accessWorkoutItems.getAll();

        // Create random number generator.
        Random randNum = new Random();

        int mockSessionId = 1; // Fake session ID

        for (var item : items) {
            int numSets = item.getSets();
            List<SessionItem> sessionLog = new ArrayList<>();

            for (int i = 0; i < numSets; i++) {
                SessionItem sessionItem = null;

                if (item instanceof RepBasedWorkoutItem) {
                    // Generate a random number of repetitions.
                    int numReps = randNum.nextInt(MAX_REPS - MIN_REPS + 1) + MIN_REPS;
                    sessionItem = new RepBasedSessionItem(item, 0.0, numReps);
                } else if (item instanceof TimeBasedWorkoutItem) {
                    // Generate a random time.
                    double time = randNum.nextDouble() * (MAX_TIME - MIN_TIME) + MIN_TIME;
                    sessionItem = new TimeBasedSessionItem(item, time);
                }

                if (sessionItem != null) {
                    sessionItems.add(sessionItem);
                    sessionLog.add(sessionItem);
                }
            }

            // Assign session items to session ID
            sessionIdMap.put(mockSessionId++, sessionLog);
        }
    }

    @Override
    public void insertSessionItem(int workoutSessionId, SessionItem sessionItem) {
        if (sessionItem != null) {
            sessionItems.add(sessionItem);

            // Assign to a session ID
            sessionIdMap.computeIfAbsent(workoutSessionId, k -> new ArrayList<>()).add(sessionItem);
        }
    }

    @Override
    public List<SessionItem> getAll() {
        return Collections.unmodifiableList(sessionItems);
    }

    @Override
    public SessionItem getSessionItemById(int sessionItemId) {
        if (sessionItemId >= 0 && sessionItemId < sessionItems.size()) {
            return sessionItems.get(sessionItemId);
        }
        return null; // Return null if ID is invalid
    }

    @Override
    public List<SessionItem> getSessionItemsBySessionId(int sessionId) {
        return sessionIdMap.getOrDefault(sessionId, Collections.emptyList());
    }
}
