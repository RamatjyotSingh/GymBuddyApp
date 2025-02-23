package comp3350.gymbuddy.objects;

import java.util.Collections;
import java.util.List;

public class WorkoutSession {
    private final long timestamp;
    private final float duration;
    private final List<SessionItem> sessionItems;
    private final WorkoutProfile profile;

    public WorkoutSession(long timestamp, float duration, List<SessionItem> sessionItems, WorkoutProfile profile) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.sessionItems = sessionItems;
        this.profile = profile;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public float getDuration() {
        return this.duration;
    }

    public List<SessionItem> getSessionItems() {
        return Collections.unmodifiableList(this.sessionItems);
    }

    public WorkoutProfile getWorkoutProfile() {
        return this.profile;
    }
}
