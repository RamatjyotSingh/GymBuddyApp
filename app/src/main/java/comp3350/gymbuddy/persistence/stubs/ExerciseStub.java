package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.IExercisePersistence;
import comp3350.gymbuddy.objects.Difficulty;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

public class ExerciseStub implements IExercisePersistence {
    private List<Exercise> exercises;

    public ExerciseStub(){
        this.exercises = new ArrayList<Exercise>();

        try{
            String content = new String(Files.readAllBytes(Paths.get("exercises_array.json")));
            JSONArray exercisesJSON = new JSONArray(content);

            for (int i = 0; i < exercisesJSON.length(); i++) {
                JSONObject ex = exercisesJSON.getJSONObject(i);

                Difficulty diff = null;
                switch(ex.getString("Difficulty Level")){
                    case "Low":
                        diff = Difficulty.Low;
                        break;

                    case "Medium":
                        diff = Difficulty.Medium;
                        break;

                    case "High":
                        diff = Difficulty.High;
                        break;
                }

                String[] primaryMuscles = ex.getString("Primary Muscles Worked").split(",");
                String[] secondaryMuscles = ex.getString("Secondary Muscles Worked").split(",");

                Exercise newExercise = new Exercise(ex.getString("Exercise Name"),diff,primaryMuscles,secondaryMuscles,ex.getString("Instructions"));
                exercises.add(newExercise);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Exercise> getAllExercises(){
        return Collections.unmodifiableList(exercises);
    }
}
