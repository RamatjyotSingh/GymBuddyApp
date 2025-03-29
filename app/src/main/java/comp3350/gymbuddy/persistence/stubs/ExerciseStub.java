package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseStub implements IExerciseDB {
    private static final int NUM_TAGS = 3; // # of tags per profile
    private static int idCounter = 0; // Simulated auto-incrementing ID
    private final List<Tag> tags;
    private final List<Exercise> exercises;

    /**
     * Initializes the stub with pre-defined tags and exercises.
     */
    public ExerciseStub() {
        tags = createTags();
        exercises = createExercises();
    }

    /**
     * Retrieves all exercises.
     * @return A list of exercises.
     */

    @Override
    public List<Exercise> getAll() {
        return Collections.unmodifiableList(exercises);
    }

    /**
     * Retrieves an exercise by its ID.
     * @param id The ID of the exercise.
     * @return The matching exercise, or null if not found.
     */
    @Override
    public Exercise getExerciseByID(int id) {
        Exercise result = null;

        // Search for the matching exercise ID.
        for (var exercise : exercises) {
            if (exercise.getID() == id) {
                result = exercise;
                break;
            }
        }

        return result;
    }

    /**
     * Creates a list of pre-defined tags for exercises.
     * @return A list of tag objects.
     */
    private List<Tag> createTags() {
        List<Tag> tags = new ArrayList<>();

        // Muscle Groups
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Upper Body", "#1D4ED8", "#D1E8FF"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Lower Body", "#6B21A8", "#E9D8FD"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Core", "#0D9488", "#B2F5EA"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Full Body", "#15803D", "#DCFCE7"));

        // Difficulty Levels
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Beginner", "#10B981", "#DCFCE7"));
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Intermediate", "#F59E0B", "#FEF3C7"));
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Advanced", "#DC2626", "#FEE2E2"));

        // Exercise Types
        tags.add(new Tag(Tag.TagType.EXERCISE_TYPE, "Cardio", "#D97706", "#FFEDD5"));
        tags.add(new Tag(Tag.TagType.EXERCISE_TYPE, "Strength", "#DB2777", "#FFD6E8"));
        tags.add(new Tag(Tag.TagType.EXERCISE_TYPE, "Flexibility", "#059669", "#D1FAE5"));
        tags.add(new Tag(Tag.TagType.EXERCISE_TYPE, "Balance", "#4338CA", "#E0E7FF"));
        tags.add(new Tag(Tag.TagType.EXERCISE_TYPE, "Endurance", "#DB2777", "#FFE4E6"));

        // Equipment
        tags.add(new Tag(Tag.TagType.EQUIPMENT, "No Equipment", "#1E3A8A", "#E0F2FE"));
        tags.add(new Tag(Tag.TagType.EQUIPMENT, "Dumbbells", "#BE185D", "#FCE7F3"));
        tags.add(new Tag(Tag.TagType.EQUIPMENT, "Resistance Bands", "#D97706", "#FEF3C7"));

        return tags;
    }

    /**
     * Creates a list of sample exercises with associated tags.
     * @return A list of pre-defined exercises.
     */

    private List<Exercise> createExercises() {
        List<Exercise> exercises = new ArrayList<>();

        // Create some exercises (Courtesy of ChatGPT)
        exercises.add(createExercise("Push-Up",
                getTags(),
                "Start in a plank position.\n" +
                        "Lower your body until your chest nearly touches the floor.\n" +
                        "Push back up to the starting position.",
                "images/push_up.png",
                false,
                false
        ));

        exercises.add(createExercise("Squat",
                getTags(),
                "Stand with feet shoulder-width apart.\n" +
                        "Lower your body by bending your knees until your thighs are parallel to the floor.\n" +
                        "Push back up to the starting position.",
                "images/squat.png",
                false,
                true
        ));

        exercises.add(createExercise("Plank",
                getTags(),
                "Hold a push-up position with your arms straight.\n" +
                        "Keep your body in a straight line from head to heels.\n" +
                        "Engage your core and hold the position.",
                "images/plank.png",
                true,
                false
        ));

        exercises.add(createExercise("Pull-Up",
                getTags(),
                "Grip a pull-up bar with palms facing away.\n" +
                        "Pull yourself up until your chin clears the bar.\n" +
                        "Lower yourself back down to the starting position.",
                "images/pull-up.png",
                false,
                false
        ));

        exercises.add(createExercise("Lunges",
                getTags(),
                "Step forward with one leg.\n" +
                        "Lower your hips until both knees are bent at 90-degree angles.\n" +
                        "Push back up to the starting position and repeat with the other leg.",
                "images/lunges.png",
                false,
                false
        ));

        exercises.add(createExercise("Deadlift",
                getTags(),
                "Stand with feet hip-width apart and grip the barbell.\n" +
                        "Keep your back straight and lift the barbell by extending your hips and knees.\n" +
                        "Lower the barbell back to the floor in a controlled motion.",
                "images/deadlift.png",
                false,
                true
        ));

        exercises.add(createExercise("Dumbbell Shoulder Press",
                getTags(),
                "Hold a dumbbell in each hand at shoulder height.\n" +
                        "Press the dumbbells overhead until your arms are fully extended.\n" +
                        "Lower the dumbbells back to the starting position.",
                "images/dumbbell-shoulder-press.png",
                false,
                true
        ));

        exercises.add(createExercise("Bicep Curls",
                getTags(),
                "Hold a dumbbell in each hand with palms facing forward.\n" +
                        "Curl the dumbbells towards your shoulders.\n" +
                        "Lower them back to the starting position.",
                "images/bicep-curls.png",
                false,
                true
        ));

        exercises.add(createExercise("Triceps Dips",
                getTags(),
                "Place your hands on a stable surface behind you.\n" +
                        "Lower your body by bending your elbows.\n" +
                        "Push yourself back up to the starting position.",
                "images/tricep-dips.png",
                false,
                false
        ));

        exercises.add(createExercise("Bent-over Rows",
                getTags(),
                "Hold a barbell or dumbbells with a slight bend in your knees.\n" +
                        "Bend at the waist and pull the weight towards your torso.\n" +
                        "Lower the weight back down in a controlled motion.",
                "images/bent-over-rows.png",
                false,
                true
        ));

        return exercises;
    }

    private Exercise createExercise(String name, List<Tag> tags, String instructions, String imagePath, boolean isTimeBased, boolean hasWeight) {
        return new Exercise(idCounter++, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    private List<Tag> getTags() {
        // Get some tags to work with.
        // Shuffle and take a subset.
        Collections.shuffle(tags);
        int newSize = Math.min(tags.size(), NUM_TAGS);
        return tags.subList(0, newSize);
    }

    @Override
    public void close() throws Exception {

    }
}
