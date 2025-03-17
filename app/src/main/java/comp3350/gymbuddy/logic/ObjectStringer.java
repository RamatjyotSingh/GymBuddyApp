package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.objects.WorkoutItem;

public class ObjectStringer {
    public ObjectStringer(){}

    public String workoutLogDetailItemString(WorkoutItem item){
        String result = "";

        if(item != null){
            if(item.isTimeBased()){
                result = "Time: " + item.getTime() + " sec";
                result += (item.getSets() > 0) ? " | Sets: " + item.getSets() : "";
            }
            else {
                result = "Reps: " + item.getReps() + ((item.getSets() > 0) ? " x " + item.getSets() + " Sets" : "") 
                         + ", Weight: " + item.getWeight() + " lbs";
            }
        }

        return result;
    }

    public String workoutBuilderItemString(WorkoutItem item){
        String result = "";

        if(item != null){
            if (item.isTimeBased()) {
                result = "Time: " + item.getTime() + " sec";
                result += (item.getSets() > 0) ? " | Sets: " + item.getSets() : "";
            } else {
                result = item.getSets() + " sets x " + item.getReps() + " reps";

                if (item.getWeight() > 0.0) {
                    result += " | " + item.getWeight() + " lbs";
                }
            }
        }

        return result;
    }
}
