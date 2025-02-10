package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.IExercisePersistence;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExerciseStub implements IExercisePersistence {
    private List<Exercise> exercises;

    public ExerciseStub() {
        this.exercises = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exercisedata.json");
            if (inputStream == null) {
                throw new RuntimeException("File not found: exercisedata.json");
            }

            // Read the JSON file content
            String content = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            JSONArray exercisesJSON = new JSONArray(content);

            for (int i = 0; i < exercisesJSON.length(); i++) {
                JSONObject ex = exercisesJSON.getJSONObject(i);

                String name = ex.getString("Exercise Name");
                String instructions = ex.getString("Instructions");

                List<Tag> tags = new ArrayList<>();

                // Convert Difficulty Level into a tag
                tags.add(new Tag(ex.getString("Difficulty Level"), "blue"));

                // Convert Primary Muscles Worked into tags
                for (String muscle : ex.getString("Primary Muscles Worked").split(",")) {
                    tags.add(new Tag(muscle.trim(), "red"));
                }

                // Convert Secondary Muscles Worked into tags
                for (String muscle : ex.getString("Secondary Muscles Worked").split(",")) {
                    tags.add(new Tag(muscle.trim(), "green"));
                }

                // Create and add the Exercise
                exercises.add(new Exercise(name, tags, instructions));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Exercise> getAllExercises() {
        return new ArrayList<>(exercises);
    }
}
