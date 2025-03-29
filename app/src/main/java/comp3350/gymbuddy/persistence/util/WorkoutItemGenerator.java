package comp3350.gymbuddy.persistence.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;

/**
 * WorkoutItemGenerator generates a list of randomized workout items.
 * It assigns exercises, sets, reps, time, and weight based on predefined ranges.
 */
public class WorkoutItemGenerator {

    // Constants defining the limits for random workout item generation.
    private static final int MIN_WORKOUT_ITEMS = 1;
    private static final int MAX_WORKOUT_ITEMS = 8;
    private static final int MIN_SETS = 2;
    private static final int MAX_SETS = 4;
    private static final int MIN_REPS = 5;
    private static final int MAX_REPS = 10;
    private static final double MIN_TIME = 50.0;
    private static final double MAX_TIME = 100.0;
    private static final double MIN_WEIGHT = 5.0;
    private static final double MAX_WEIGHT = 25.0;

    private final int seed; // Seed for randomness to ensure reproducibility.

    /**
     * Constructor initializes the generator with a specific seed for consistency.
     * @param seed The seed for the random number generator.
     */
    public WorkoutItemGenerator(int seed) {
        this.seed = seed;
    }

    /**
     * Generates a randomized list of workout items.
     * @return A list of randomly generated WorkoutItem objects.
     */
    public List<WorkoutItem> generate() {
        List<WorkoutItem> workoutItems = new ArrayList<>();
        Random random = new Random(seed);

        // Retrieve a list of exercises to assign to workout items.
        List<Exercise> exercises = new ExerciseStub().getAll();

        // Determine the number of workout items to generate.
        int count = random.nextInt(MAX_WORKOUT_ITEMS - MIN_WORKOUT_ITEMS) + MIN_WORKOUT_ITEMS;

        for (int i = 0; i < count; i++) {
            // Select a random exercise from the available list.
            Exercise exercise = exercises.get(random.nextInt(exercises.size()));
            int sets = random.nextInt(MAX_SETS - MIN_SETS) + MIN_SETS;

            if (exercise.isTimeBased()) {
                // Generate time-based workout item.
                double time = random.nextDouble() * (MAX_TIME - MIN_TIME) + MIN_TIME;
                workoutItems.add(new WorkoutItem(exercise, sets, time));
            } else {
                // Generate rep-based workout item.
                int reps = random.nextInt(MAX_REPS - MIN_REPS) + MIN_REPS;

                // Generate weight if the exercise requires it.
                double weight = 0.0;
                if (exercise.hasWeight()) {
                    weight = random.nextDouble() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
                }

                workoutItems.add(new WorkoutItem(exercise, sets, reps, weight));
            }
        }

        return workoutItems;
    }
}
