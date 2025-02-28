package comp3350.gymbuddy.objects;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WorkoutSession {
    public static final long MAX_SESSION_LENGTH = 1000 * 60 * 60 * 2; //2hrs in milliseconds
    private final Date date;
    private final long startTime;
    private final long endTime;
    private final List<SessionItem> sessionItems;
    private final WorkoutProfile profile;

    public WorkoutSession(Date date, long startTime, long endTime, List<SessionItem> sessionItems, WorkoutProfile profile) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionItems = sessionItems;
        this.profile = profile;
    }

    public String getDate(){
        return this.date.toString();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public float getDuration() {
        return (float)(this.endTime - this.startTime);
    }

    public String getDurationString(){
        float duration = getDuration();
        duration /= 60000; // convert to minutes
        int durationMin = (int)duration;
        int durationSec = (int)((duration - durationMin) * 60);

        return durationMin + " min " + durationSec + " sec ";
    }

    public List<SessionItem> getSessionItems() {
        return Collections.unmodifiableList(this.sessionItems);
    }

    public WorkoutProfile getWorkoutProfile() {
        return this.profile;
    }
}
