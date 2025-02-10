package comp3350.gymbuddy.persistence.stubs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.ExerciseService;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class WorkoutItemStub implements IWorkoutItemPersistence {
    private List<WorkoutItem> workoutItems;

    public WorkoutItemStub(){
        this.workoutItems = new ArrayList<>();

        ExerciseService exService = new ExerciseService();
        List<Exercise> exercises = exService.getAllExercises();

        try{
            String content = new String(Files.readAllBytes(Paths.get("workout-item.json")));
            JSONArray workoutItemsJSON = new JSONArray(content);

            for(int i=0; i<workoutItemsJSON.length(); i++){
                JSONObject item = workoutItemsJSON.getJSONObject(i);

                // look through exercises DB stub for corresponding exercise
                // just so these workout items link to an existing exercise
                String exerciseName = item.getString("Exercise Name");
                Exercise toAdd = null;
                for(Exercise e : exercises){
                    if(e.getName().equalsIgnoreCase(exerciseName)){
                        toAdd = e;
                        break;
                    }
                }

                double weight = (item.getDouble("Weight") >= 0) ? (item.getDouble("Weight")) : 0.0;
                int repetitions = (item.getInt("Repetitions") >= 0) ? (item.getInt("Repetitions")) : 0;
                int sets = (item.getInt("Sets") >= 0) ? (item.getInt("Sets")) : 1;
                double time = (item.getDouble("Time") >= 0) ? (item.getDouble("Time")) : 0.0;

                workoutItems.add(new WorkoutItem(toAdd, sets, repetitions, weight, time));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<WorkoutItem> getAllWorkoutItems(){
        return new ArrayList<>(this.workoutItems);
    }
}
