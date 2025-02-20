package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class WorkoutItemStub implements IWorkoutItemPersistence {
    final private List<RepBasedWorkoutItem> workoutItems;

    public WorkoutItemStub() {
        workoutItems = new ArrayList<>();

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Push-Up"),
                4,
                12,
                0.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Squat"),
                4,
                10,
                0.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Pull-Up"),
                3,
                8,
                0.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Deadlift"),
                4,
                5,
                80.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Dumbbell Shoulder Press"),
                3,
                10,
                15.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Bicep Curls"),
                3,
                12,
                12.5));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Triceps Dips"),
                3,
                10,
                0.0));

        workoutItems.add(new RepBasedWorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Bent-Over Rows"),
                4,
                8,
                50.0));

    }

    public List<WorkoutItem> getAllWorkoutItems() {
        return Collections.unmodifiableList(workoutItems);
    }
}
