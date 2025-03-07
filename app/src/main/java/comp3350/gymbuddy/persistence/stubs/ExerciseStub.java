package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

/**
 * ExerciseStub simulates a database of exercises and tags for testing purposes.
 */
public class ExerciseStub implements IExerciseDB {
    private static final int NUM_TAGS = 3; // Number of tags per exercise
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

        // Adding various types of tags
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Upper Body", "#1D4ED8", "#D1E8FF"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Lower Body", "#6B21A8", "#E9D8FD"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Core", "#0D9488", "#B2F5EA"));
        tags.add(new Tag(Tag.TagType.MUSCLE_GROUP, "Full Body", "#15803D", "#DCFCE7"));

        // Difficulty Levels
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Beginner", "#10B981", "#DCFCE7"));
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Intermediate", "#F59E0B", "#FEF3C7"));
        tags.add(new Tag(Tag.TagType.DIFFICULTY, "Advanced", "#DC2626", "#FEE2E2"));

        return tags;
    }

    /**
     * Creates a list of sample exercises with associated tags.
     * @return A list of pre-defined exercises.
     */
    private List<Exercise> createExercises() {
        List<Exercise> exercises = new ArrayList<>();

        // Sample exercises with instructions and tags
        exercises.add(createExercise("Push-Up", getTags(), "Lower body to the floor and push back up.", "images/push_up.png", false, false));
        exercises.add(createExercise("Squat", getTags(), "Lower body by bending knees, then rise.", "images/squat.png", false, true));
        exercises.add(createExercise("Plank", getTags(), "Hold a straight body posture on forearms.", "images/plank.png", true, false));
        exercises.add(createExercise("Pull-Up", getTags(), "Pull body up using a bar until chin is above it.", "images/pull-up.png", false, false));
        exercises.add(createExercise("Lunges", getTags(), "Step forward and lower hips, then return.", "images/lunges.png", false, false));

        return exercises;
    }

    /**
     * Creates an exercise object with unique ID.
     * @param name The name of the exercise.
     * @param tags A list of tags associated with the exercise.
     * @param instructions Instructions for performing the exercise.
     * @param imagePath The image path for the exercise.
     * @param isTimeBased Whether the exercise is time-based.
     * @param hasWeight Whether the exercise involves weight.
     * @return A new Exercise object.
     */
    private Exercise createExercise(String name, List<Tag> tags, String instructions, String imagePath, boolean isTimeBased, boolean hasWeight) {
        return new Exercise(idCounter++, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    /**
     * Retrieves a random subset of predefined tags for an exercise.
     * @return A list containing a random selection of tags.
     */
    private List<Tag> getTags() {
        Collections.shuffle(tags);
        int newSize = Math.min(tags.size(), NUM_TAGS);
        return tags.subList(0, newSize);
    }
}
