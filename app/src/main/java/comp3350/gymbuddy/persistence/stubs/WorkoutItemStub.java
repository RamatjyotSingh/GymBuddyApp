package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutItemPersistence;

public class WorkoutItemStub implements IWorkoutItemPersistence {
    private final List<WorkoutItem> workoutItems;
    private final List<Integer> profileIds; // Mocked storage for profile ID associations

    public WorkoutItemStub() {
        workoutItems = new ArrayList<>();
        profileIds = new ArrayList<>(); // Simulating profile connections

        int mockProfileId = 1; // Fake profile ID since stubs don't have a real database

        workoutItems.add(new WorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Push-Up"),
                4,
                12,
                0.0));
        profileIds.add(mockProfileId);

        workoutItems.add(new WorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Squat"),
                4,
                10,
                0.0));
        profileIds.add(mockProfileId);

        workoutItems.add(new WorkoutItem(
                Services.getExercisePersistence().getExerciseByName("Pull-Up"),
                3,
                8,
                0.0));
        profileIds.add(mockProfileId);
    }

    @Override
    public List<WorkoutItem> getAll() {
        return Collections.unmodifiableList(workoutItems);
    }

    @Override
    public void insertWorkoutItem(WorkoutItem item, int profileId) {
        if (item != null) {
            workoutItems.add(item);
            profileIds.add(profileId); // Associate the item with a profile ID
        }
    }

    @Override
    public List<WorkoutItem> getWorkoutItemsByProfileId(int profileId) {
        List<WorkoutItem> filteredItems = new ArrayList<>();

        for (int i = 0; i < workoutItems.size(); i++) {
            if (profileIds.get(i) == profileId) {
                filteredItems.add(workoutItems.get(i));
            }
        }

        return Collections.unmodifiableList(filteredItems);
    }

    @Override
    public WorkoutItem getWorkoutItemById(int workoutItemId) {
        // In a real DB, ID would be unique, but in the stub, we treat indices as ID placeholders
        if (workoutItemId >= 0 && workoutItemId < workoutItems.size()) {
            return workoutItems.get(workoutItemId);
        }
        return null; // If ID is out of range, return null
    }
}
