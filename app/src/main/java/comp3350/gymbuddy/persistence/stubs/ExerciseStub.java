package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.IExercisePersistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class ExerciseStub implements IExercisePersistence {
    private List<Exercise> exercises;

    public ExerciseStub() {
        this.exercises = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get("exercise.json")));
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
        return new ArrayList<Exercise>(this.exercises);
    }
}
