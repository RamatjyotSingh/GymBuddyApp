package comp3350.gymbuddy.objects;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class WorkoutSession {
    private final int id;
    private final long startTime;
    private final long endTime;
    private final List<WorkoutItem> workoutItems;
    private final WorkoutProfile profile;

    public WorkoutSession(int id, long startTime, long endTime, List<WorkoutItem> workoutItems, WorkoutProfile profile) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workoutItems = workoutItems;
        this.profile = profile;
    }

    public String getDate(){
        ZonedDateTime dateTime = Instant.ofEpochMilli(this.startTime).atZone(ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy");

        return dateTime.format(formatter);
    }

    public int getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return endTime - startTime;
    }

    public String getDurationString(){
        double duration = getDuration();
        duration /= 60000; // convert to minutes
        int durationMin = (int)duration;
        int durationSec = (int)((duration - durationMin) * 60);

        return durationMin + " min " + durationSec + " sec ";
    }

    public List<WorkoutItem> getWorkoutItems() {
        return Collections.unmodifiableList(this.workoutItems);
    }

    public WorkoutProfile getWorkoutProfile() {
        return profile;
    }

    public long getEndTime() {
        return endTime;
    }


}
