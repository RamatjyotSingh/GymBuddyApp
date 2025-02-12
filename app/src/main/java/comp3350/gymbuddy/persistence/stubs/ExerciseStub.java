package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.IExercisePersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseStub implements IExercisePersistence {
    private List<Exercise> exercises;

    private Exercise createExercise(String name, String[] tagNames, String instructions) {
        List<Tag> tags = new ArrayList<Tag>();

        for (var tagName : tagNames) {
            tags.add(Services.getTagPersistence().getTagByName(tagName));
        }

        return new Exercise(name, tags, instructions);
    }

    public ExerciseStub() {
        exercises = new ArrayList<>();

        exercises.add(createExercise("Push-Up",
                new String[]{"Chest", "Triceps", "Shoulders", "Core", "Intermediate"},
                "Start in a plank position, lower your body until your chest nearly touches the floor, then push back up."));

        exercises.add(createExercise("Squat",
                new String[]{"Quadriceps", "Glutes", "Hamstrings", "Core", "Intermediate"},
                "Stand with feet shoulder-width apart, lower your body by bending your knees until your thighs are parallel to the floor, then push back up."));

        exercises.add(createExercise("Plank",
                new String[]{"Core", "Shoulders", "Lower Back", "Balance", "Beginner"},
                "Hold a push-up position with your arms straight and your body in a straight line from head to heels. Keep your core engaged."));

        exercises.add(createExercise("Pull-Up",
                new String[]{"Back", "Biceps", "Forearms", "Intermediate"},
                "Grip a pull-up bar with palms facing away, pull yourself up until your chin clears the bar, then lower yourself back down."));

        exercises.add(createExercise("Lunges",
                new String[]{"Quadriceps", "Glutes", "Hamstrings", "Balance", "Beginner"},
                "Step forward with one leg, lowering your hips until both knees are bent at 90-degree angles, then push back to standing. Repeat with the other leg."));

        exercises.add(createExercise("Deadlift",
                new String[]{"Lower Back", "Glutes", "Hamstrings", "Forearms", "Advanced"},
                "Stand with feet hip-width apart, grip the barbell, hinge at the hips, and lift the bar by straightening your legs and extending your back."));

        exercises.add(createExercise("Dumbbell Shoulder Press",
                new String[]{"Shoulders", "Triceps", "Core", "Intermediate"},
                "Hold dumbbells at shoulder height, press them overhead until arms are fully extended, then lower back to the starting position."));

        exercises.add(createExercise("Calf Raises",
                new String[]{"Calves", "Balance", "Beginner"},
                "Stand with feet shoulder-width apart, lift your heels off the ground until you’re on your toes, then slowly lower back down."));

        exercises.add(createExercise("Bent-Over Rows",
                new String[]{"Back", "Biceps", "Forearms", "Intermediate"},
                "Bend at the hips while keeping your back straight, pull a barbell or dumbbells towards your torso, then lower back down."));

        exercises.add(createExercise("Russian Twists",
                new String[]{"Core", "Balance", "Intermediate"},
                "Sit on the floor with knees bent, lean back slightly, twist your torso side to side while holding a weight or medicine ball."));

        exercises.add(createExercise("Leg Raises",
                new String[]{"Core", "Lower Back", "Beginner"},
                "Lie flat on your back, lift your legs towards the ceiling while keeping them straight, then slowly lower them back down."));

        exercises.add(createExercise("Bicep Curls",
                new String[]{"Biceps", "Forearms", "Beginner"},
                "Hold a dumbbell in each hand with palms facing forward, curl the weights towards your shoulders, then lower back down."));

        exercises.add(createExercise("Triceps Dips",
                new String[]{"Triceps", "Shoulders", "Chest", "Intermediate"},
                "Grip parallel bars or a bench, lower your body by bending your elbows, then push back up to the starting position."));

    }

    @Override
    public List<Exercise> getAllExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public Exercise getExerciseByName(String name) {
        for (var exercise : exercises) {
            if (exercise.getName().equals(name)) {
                return exercise;
            }
        }
        throw new RuntimeException("Exercise not found");
    }
}
