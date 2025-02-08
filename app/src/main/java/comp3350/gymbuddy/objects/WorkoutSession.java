package comp3350.gymbuddy.objects;

import java.util.List;

public class WorkoutSession {
    private final int timestamp;
    private final float duration;
    private final List<WorkoutItem> items;
    private final WorkoutProfile profile;

    public WorkoutSession(int timestamp, float duration, List<WorkoutItem> items, WorkoutProfile profile) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.items = items;
        this.profile = profile;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public float getDuration() {
        return duration;
    }

    public List<WorkoutItem> getItems() {
        return items;
    }

    public WorkoutProfile getProfile() {
        return profile;
    }

    public void addItem(WorkoutItem item) {
        items.add(item);
    }
}
