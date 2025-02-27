/*
package comp3350.gymbuddy.persistence.hsqldb;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.RepBasedSessionItem;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.objects.TimeBasedSessionItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.ISessionItemPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SessionItemHSQLDB implements ISessionItemPersistence {

    private final Connection connection;
    private static final int MIN_REPS = 1;
    private static final int MAX_REPS = 18;
    private static final double MIN_TIME = 5.0;
    private static final double MAX_TIME = 60.0;

    public SessionItemHSQLDB(final Connection connection) {
        this.connection = connection;
    }

    private void populateSessionItem() {
        List<WorkoutItem> items = Services.getWorkoutItemPersistence().getAllWorkoutItems();
        Random randNum = new Random();

        for (var item : items) {
            int numSets = item.getSets();
            for (int i = 0; i < numSets; i++) {
                SessionItem sessionItem = null;
                if (item instanceof RepBasedWorkoutItem) {
                    int numReps = randNum.nextInt(MAX_REPS - MIN_REPS) + MIN_REPS;
                    sessionItem = new RepBasedSessionItem(item, 0.0, numReps);
                } else if (item instanceof TimeBasedWorkoutItem) {
                    double time = randNum.nextDouble() * (MAX_TIME - MIN_TIME) + MIN_TIME;
                    sessionItem = new TimeBasedSessionItem(item, time);
                }
            }
        }
    }

    @Override
    public List<SessionItem> getAllSessionItems() {
        List<SessionItem> sessionItems = new ArrayList<>();
        String sql = "SELECT * FROM PUBLIC.SESSIONITEM";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int workoutItemId = rs.getInt("workoutItemId");
                String type = rs.getString("type");

                WorkoutItem associatedWorkoutItem = Services.getWorkoutItemPersistence().getWorkoutItemById(workoutItemId);

                if ("Rep".equalsIgnoreCase(type)) {
                    int reps = rs.getInt("reps");
                    double weight = rs.getDouble("weight");
                    sessionItems.add(new RepBasedSessionItem(associatedWorkoutItem, weight, reps));
                } else if ("Time".equalsIgnoreCase(type)) {
                    double time = rs.getDouble("time");
                    sessionItems.add(new TimeBasedSessionItem(associatedWorkoutItem, time));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(sessionItems);
    }
}
*/
