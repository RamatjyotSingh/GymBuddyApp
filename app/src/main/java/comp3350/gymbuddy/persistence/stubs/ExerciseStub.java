package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class ExerciseStub implements IExercisePersistence {
    private static int id_counter = 0;
    private final List<Exercise> exercises;

    private Exercise createExercise(String name, List<Tag> tags, ArrayList<String> instructions, String imagePath, boolean isTimeBased, boolean hasWeight) {
        return new Exercise(id_counter++, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    public ExerciseStub() {
        exercises = new ArrayList<>();

        exercises.add(createExercise("Push-Up",
                getTags(),
                new ArrayList<>(List.of(
                        "Start in a plank position.",
                        "Lower your body until your chest nearly touches the floor.",
                        "Push back up to the starting position.")),
                "push_up.png",
                false,
                false
        ));

        exercises.add(createExercise("Squat",
                getTags(),
                new ArrayList<>(List.of(
                        "Stand with feet shoulder-width apart.",
                        "Lower your body by bending your knees until your thighs are parallel to the floor.",
                        "Push back up to the starting position.")),
                "squat.png",
                false,
                true
        ));

        exercises.add(createExercise("Plank",
                getTags(),
                new ArrayList<>(List.of(
                        "Hold a push-up position with your arms straight.",
                        "Keep your body in a straight line from head to heels.",
                        "Engage your core and hold the position.")),
                "plank.png",
                true,
                false
        ));

        exercises.add(createExercise("Pull-Up",
                getTags(),
                new ArrayList<>(List.of(
                        "Grip a pull-up bar with palms facing away.",
                        "Pull yourself up until your chin clears the bar.",
                        "Lower yourself back down to the starting position.")),
                "pull-up.png",
                false,
                false
        ));

        exercises.add(createExercise("Lunges",
                getTags(),
                new ArrayList<>(List.of(
                        "Step forward with one leg.",
                        "Lower your hips until both knees are bent at 90-degree angles.",
                        "Push back up to the starting position and repeat with the other leg.")),
                "lunges.png",
                false,
                false
        ));

        // Add more exercises similarly
    }

    private List<Tag> getTags() {
        TagStub tagStub = new TagStub();
        return tagStub.getTagsByExerciseID(1); // Just return all tags for now, or use logic to pick random ones
    }

    @Override
    public List<Exercise> getAll() {
        return Collections.unmodifiableList(exercises);
    }

    @Override
    public Exercise getExerciseByName(String name) {
        for (var exercise : exercises) {
            if (exercise.getName().equals(name)) {
                return exercise;
            }
        }
        return null; // Return null if no exercise found
    }

    public Exercise getExerciseByID(int id) {
        Exercise result = null;

        for (int i = 0; i < exercises.size() && result == null; i++) {
            if (exercises.get(i).getID() == id) {
                result = exercises.get(i);
            }
        }

        return result;
    }
}
