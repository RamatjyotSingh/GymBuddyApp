package comp3350.gymbuddy.persistence.stubs;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Set;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.ISetPersistence;

public class SetStub implements ISetPersistence {
    final private List<Set> sets;

    public SetStub(){
        sets = new ArrayList<>();

        List<WorkoutItem> items = Services.getWorkoutItemPersistence().getAllWorkoutItems();

        for(int i=0; i<items.size(); i++){
            WorkoutItem wi = items.get(i);
            int numSets = wi.getSets();

            Random randNum = new Random();

            for(int j=0; j<numSets; j++){
                // random # of reps, varies by at most 1 from original # of reps
                int randReps = randNum.nextInt(3) + wi.getReps() - 1;

                sets.add(new Set(wi.getWeight(), randReps, wi.getTime(), wi));
            }
        }
    }

    public List<Set> getAllSets(){
        return Collections.unmodifiableList(sets);
    }
}
