package comp3350.gymbuddy.objects;

import java.util.List;

public class WorkoutSession {
    private final int timestamp;
    private final float duration;
    private final List<Set> sets;
    private final WorkoutProfile profile;

    public WorkoutSession(int timestamp, float duration, List<Set> sets, WorkoutProfile profile) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.sets = sets;
        this.profile = profile;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public float getDuration() {
        return duration;
    }

    public List<Set> getItems() {
        return sets;
    }

    public WorkoutProfile getProfile() {
        return profile;
    }

    public void addItem(Set sets) {
        this.sets.add(sets);
    }
}
