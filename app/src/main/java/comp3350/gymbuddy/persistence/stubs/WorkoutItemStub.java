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
        this.workoutItems = new ArrayList<WorkoutItem>();

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

                Double weight = (item.getDouble("Weight") >= 0) ? (item.getDouble("Weight")) : null;
                Integer repetitions = (item.getInt("Repetitions") >= 0) ? (item.getInt("Repetitions")) : null;
                Integer sets = (item.getInt("Sets") >= 0) ? (item.getInt("Sets")) : null;
                Double time = (item.getDouble("Time") >= 0) ? (item.getDouble("Time")) : null;

                workoutItems.add(new WorkoutItem(toAdd, weight, repetitions, sets, time));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<WorkoutItem> getAllWorkoutItems(){
        return new ArrayList<WorkoutItem>(this.workoutItems);
    }
}
