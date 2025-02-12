package comp3350.gymbuddy.objects;

import java.util.List;

public class WorkoutSession {
    private final long timestamp;
    private final float duration;
    private final List<Set> sets;
    private final WorkoutProfile profile;

    public WorkoutSession(long timestamp, float duration, List<Set> sets, WorkoutProfile profile) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.sets = sets;
        this.profile = profile;
    }
}
