package comp3350.gymbuddy.objects;

import java.util.Collections;
import java.util.List;

public class WorkoutSession {
    public static final int MAX_SESSION_LENGTH = 120;
    private final long startTime;
    private final long endTime;
    private final List<SessionItem> sessionItems;
    private final WorkoutProfile profile;

    public WorkoutSession(long startTime, long endTime, List<SessionItem> sessionItems, WorkoutProfile profile) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionItems = sessionItems;
        this.profile = profile;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public float getDuration() {
        return (float)(this.endTime - this.startTime);
    }

    public List<SessionItem> getSessionItems() {
        return Collections.unmodifiableList(this.sessionItems);
    }

    public WorkoutProfile getWorkoutProfile() {
        return this.profile;
    }
}
