package comp3350.gymbuddy.persistence.stubs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.AccessExercises;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class WorkoutItemStub implements IWorkoutItemPersistence {
    private List<WorkoutItem> workoutItems;

    public WorkoutItemStub() {
        this.workoutItems = new ArrayList<>();

        // Fetch exercises internally
        AccessExercises exService = new AccessExercises();
        List<Exercise> exercises = exService.getAllExercises();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("workoutitemdata.json")) {
            if (inputStream == null) {
                throw new RuntimeException("File not found: workoutitemdata.json");
            }

            // Read JSON file as a string
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            JSONArray workoutItemsJSON = new JSONArray(content);

            for (int i = 0; i < workoutItemsJSON.length(); i++) {
                JSONObject item = workoutItemsJSON.getJSONObject(i);

                // Look through provided exercises list
                String exerciseName = item.getString("Exercise Name");
                Exercise toAdd = exercises.stream()
                        .filter(e -> e.getName().equalsIgnoreCase(exerciseName))
                        .findFirst()
                        .orElse(null); // Default to null

                // Ensure we don’t add a workoutItem with a null exercise
                if (toAdd != null) {
                    double weight = Math.max(item.optDouble("Weight", 0.0), 0.0);
                    int repetitions = Math.max(item.optInt("Repetitions", 0), 0);
                    int sets = Math.max(item.optInt("Sets", 1), 1);
                    double time = Math.max(item.optDouble("Time", 0.0), 0.0);

                    workoutItems.add(new WorkoutItem(toAdd, sets, repetitions, weight, time));
                }
            }
        } catch (IOException | JSONException e) {
            System.err.println("Error loading workout items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<WorkoutItem> getAllWorkoutItems() {
        return new ArrayList<>(this.workoutItems);
    }
}
