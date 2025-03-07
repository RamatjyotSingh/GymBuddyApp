package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.objects.WorkoutItem;

public class ObjectStringer {
    public ObjectStringer(){}

    public String workoutLogDetailItemString(WorkoutItem item){
        String result = "";

        if(item != null){
            if(item.isTimeBased()){
                result = "Time: " + item.getTime() + " sec";
            }
            else {
                result = "Reps: " + item.getReps() + ", Weight: " + item.getWeight() + " kg";
            }
        }

        return result;
    }

    public String workoutBuilderItemString(WorkoutItem item){
        String result = "";

        if(item != null){
            if (item.isTimeBased()) {
                result = item.getTime() + " sec";
            } else {
                result = item.getSets() + " sets x " + item.getReps() + " reps";

                if (item.getWeight() > 0.0) {
                    result += " | " + item.getWeight() + " kg";
                }
            }
        }

        return result;
    }
}
