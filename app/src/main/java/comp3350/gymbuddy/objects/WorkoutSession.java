package comp3350.gymbuddy.objects;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class WorkoutSession {
    public static final long MAX_SESSION_LENGTH = 1000 * 60 * 60 * 2; //2hrs in milliseconds
    private int id;
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

    public int getID(){
        return id;
    }

    public String getDate(){
        ZonedDateTime dateTime = Instant.ofEpochMilli(this.startTime).atZone(ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy");

        return dateTime.format(formatter);
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
