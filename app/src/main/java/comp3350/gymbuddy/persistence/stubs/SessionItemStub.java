package comp3350.gymbuddy.persistence.stubs;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;

public class SessionItemStub implements ISessionItemPersistence {
    private static int idCounter = 0;
    private static final int MIN_REPS = 1;
    private static final int MAX_REPS = 18;
    private static final double MIN_TIME = 5.0;
    private static final double MAX_TIME = 60.0;
    private final List<SessionItem> sessionItems;


    public SessionItemStub() {
        sessionItems = new ArrayList<>();

        AccessWorkoutItems accessWorkoutItems = new AccessWorkoutItems();
        List<WorkoutItem> items = accessWorkoutItems.getAll();

        // Create random number generator.
        Random randNum = new Random();

        for (var item : items) {
            int numSets = item.getSets();

            for (int i = 0; i < numSets; i++) {
                SessionItem sessionItem = null;

                if (item instanceof RepBasedWorkoutItem) {
                    // Generate a random number of repetitions.
                    int numReps = randNum.nextInt(MAX_REPS - MIN_REPS) + MIN_REPS;
                    sessionItem = new RepBasedSessionItem(idCounter++, item, 0.0, numReps);
                } else if (item instanceof TimeBasedWorkoutItem) {
                    // Generate a random time.
                    double time = randNum.nextDouble() * (MAX_TIME - MIN_TIME) + MIN_TIME;
                    sessionItem = new TimeBasedSessionItem(idCounter++, item, time);
                }

                if (sessionItem != null) {
                    sessionItems.add(sessionItem);
                }
            }
        }
    }

    public List<SessionItem> getAll()
    {
        return Collections.unmodifiableList(sessionItems);
    }
}
