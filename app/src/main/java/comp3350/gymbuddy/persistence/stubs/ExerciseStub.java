package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.PersistenceManager;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class ExerciseStub implements IExercisePersistence {
    private static int idCounter = 0;
    private final List<Exercise> exercises;

    private Exercise createExercise(String name, List<Tag> tags, ArrayList<String> instructions, String imagePath, boolean isTimeBased, boolean hasWeight) {
        return new Exercise(idCounter++, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    public ExerciseStub() {
        exercises = new ArrayList<>();

        exercises.add(createExercise("Push-Up",
                getTags(),
                new ArrayList<>(List.of(
                        "Start in a plank position.",
                        "Lower your body until your chest nearly touches the floor.",
                        "Push back up to the starting position.")),
                "images/push_up.png",
                false,
                false
        ));

        exercises.add(createExercise("Squat",
                getTags(),
                new ArrayList<>(List.of(
                        "Stand with feet shoulder-width apart.",
                        "Lower your body by bending your knees until your thighs are parallel to the floor.",
                        "Push back up to the starting position.")),
                "images/squat.png",
                false,
                true
        ));

        exercises.add(createExercise("Plank",
                getTags(),
                new ArrayList<>(List.of(
                        "Hold a push-up position with your arms straight.",
                        "Keep your body in a straight line from head to heels.",
                        "Engage your core and hold the position.")),
                "images/plank.png",
                true,
                false
        ));

        exercises.add(createExercise("Pull-Up",
                getTags(),
                new ArrayList<>(List.of(
                        "Grip a pull-up bar with palms facing away.",
                        "Pull yourself up until your chin clears the bar.",
                        "Lower yourself back down to the starting position.")),
                "images/pull-up.png",
                false,
                false
        ));

        exercises.add(createExercise("Lunges",
                getTags(),
                new ArrayList<>(List.of(
                        "Step forward with one leg.",
                        "Lower your hips until both knees are bent at 90-degree angles.",
                        "Push back up to the starting position and repeat with the other leg.")),
                "images/lunges.png",
                false,
                false
        ));

        exercises.add(createExercise("Deadlift",
                getTags(),
                new ArrayList<>(List.of(
                        "Stand with feet hip-width apart and grip the barbell.",
                        "Keep your back straight and lift the barbell by extending your hips and knees.",
                        "Lower the barbell back to the floor in a controlled motion.")),
                "images/deadlift.png",
                false,
                true
        ));

        exercises.add(createExercise("Dumbbell Shoulder Press",
                getTags(),
                new ArrayList<>(List.of(
                        "Hold a dumbbell in each hand at shoulder height.",
                        "Press the dumbbells overhead until your arms are fully extended.",
                        "Lower the dumbbells back to the starting position.")),
                "images/dumbbell-shoulder-press.png",
                false,
                true
        ));

        exercises.add(createExercise("Bicep Curls",
                getTags(),
                new ArrayList<>(List.of(
                        "Hold a dumbbell in each hand with palms facing forward.",
                        "Curl the dumbbells towards your shoulders.",
                        "Lower them back to the starting position.")),
                "images/bicep-curls.png",
                false,
                true
        ));

        exercises.add(createExercise("Triceps Dips",
                getTags(),
                new ArrayList<>(List.of(
                        "Place your hands on a stable surface behind you.",
                        "Lower your body by bending your elbows.",
                        "Push yourself back up to the starting position.")),
                "images/tricep-dips.png",
                false,
                false
        ));

        exercises.add(createExercise("Bent-over Rows",
                getTags(),
                new ArrayList<>(List.of(
                        "Hold a barbell or dumbbells with a slight bend in your knees.",
                        "Bend at the waist and pull the weight towards your torso.",
                        "Lower the weight back down in a controlled motion.")),
                "images/bent-over-rows.png",
                false,
                true
        ));
    }

    private List<Tag> getTags() {
        return (new TagStub()).getTagsByExerciseID(1);
    }

    @Override
    public List<Exercise> getAll() {
        return Collections.unmodifiableList(exercises);
    }

    @Override
    public Exercise getExerciseByName(String name) {
        for (var exercise : exercises) {
            if (exercise.getName().equalsIgnoreCase(name)) {
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
