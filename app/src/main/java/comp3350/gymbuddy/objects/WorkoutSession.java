package comp3350.gymbuddy.objects;

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
}
