package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.IExercisePersistence;


public class ExerciseStub implements IExercisePersistence {
    private static int id_counter = 0;
    private List<Exercise> exercises;

    private Exercise createExercise(String name, String[] tagNames, ArrayList<String> instructions, String imagePath) {
        List<Tag> tags = new ArrayList<>();


        for (var tagName : tagNames) {
            tags.add(Services.getTagPersistence().getTagByName(tagName));
        }

        return new Exercise(id_counter++, name, tags, instructions, imagePath);
    }

    public ExerciseStub() {
        exercises = new ArrayList<>();

        exercises.add(createExercise("Push-Up",
                new String[]{"Chest", "Triceps", "Shoulders", "Core", "Intermediate"},
                new ArrayList<>(List.of(
                        "Start in a plank position.",
                        "Lower your body until your chest nearly touches the floor.",
                        "Push back up to the starting position.")),
                "push_up.png"
                ));

        exercises.add(createExercise("Squat",
                new String[]{"Quadriceps", "Glutes", "Hamstrings", "Core", "Intermediate"},
                new ArrayList<>(List.of(
                        "Stand with feet shoulder-width apart.",
                        "Lower your body by bending your knees until your thighs are parallel to the floor.",
                        "Push back up to the starting position.")),
                "squat.png"
                ));

        exercises.add(createExercise("Plank",
                new String[]{"Core", "Shoulders", "Lower Back", "Balance", "Beginner"},
                new ArrayList<>(List.of(
                        "Hold a push-up position with your arms straight.",
                        "Keep your body in a straight line from head to heels.",
                        "Engage your core and hold the position.")),
                "plank.png"
                ));

        exercises.add(createExercise("Pull-Up",
                new String[]{"Back", "Biceps", "Forearms", "Intermediate"},
                new ArrayList<>(List.of(
                        "Grip a pull-up bar with palms facing away.",
                        "Pull yourself up until your chin clears the bar.",
                        "Lower yourself back down to the starting position.")),
                "pull-up.png"
                ));

        exercises.add(createExercise("Lunges",
                new String[]{"Quadriceps", "Glutes", "Hamstrings", "Balance", "Beginner"},
                new ArrayList<>(List.of(
                        "Step forward with one leg.",
                        "Lower your hips until both knees are bent at 90-degree angles.",
                        "Push back up to the starting position and repeat with the other leg.")),
                "lunges.png"
                ));

        exercises.add(createExercise("Deadlift",
                new String[]{"Lower Back", "Glutes", "Hamstrings", "Forearms", "Advanced"},
                new ArrayList<>(List.of(
                        "Stand with feet hip-width apart.",
                        "Grip the barbell and hinge at the hips.",
                        "Lift the bar by straightening your legs and extending your back.")),
                "deadlift.png"
                ));

        exercises.add(createExercise("Dumbbell Shoulder Press",
                new String[]{"Shoulders", "Triceps", "Core", "Intermediate"},
                new ArrayList<>(List.of(
                        "Hold dumbbells at shoulder height.",
                        "Press them overhead until arms are fully extended.",
                        "Lower back to the starting position.")),
                "dumbbell-shoulder-press.png"
                ));

        exercises.add(createExercise("Calf Raises",
                new String[]{"Calves", "Balance", "Beginner"},
                new ArrayList<>(List.of(
                        "Stand with feet shoulder-width apart.",
                        "Lift your heels off the ground until you’re on your toes.",
                        "Slowly lower back down.")),
                "calf-raises.png"
                ));

        exercises.add(createExercise("Bent-Over Rows",
                new String[]{"Back", "Biceps", "Forearms", "Intermediate"},
                new ArrayList<>(List.of(
                        "Bend at the hips while keeping your back straight.",
                        "Pull a barbell or dumbbells towards your torso.",
                        "Lower back down to the starting position.")),
                "bent-over-rows.png"
                ));

        exercises.add(createExercise("Russian Twists",
                new String[]{"Core", "Balance", "Intermediate"},
                new ArrayList<>(List.of(
                        "Sit on the floor with knees bent.",
                        "Lean back slightly and twist your torso side to side.",
                        "Hold a weight or medicine ball for added resistance.")),
                "russian-twists.png"
                ));

        exercises.add(createExercise("Leg Raises",
                new String[]{"Core", "Lower Back", "Beginner"},
                new ArrayList<>(List.of(
                        "Lie flat on your back.",
                        "Lift your legs towards the ceiling while keeping them straight.",
                        "Slowly lower them back down without touching the floor.")),
                "leg-raises.png"
                ));

        exercises.add(createExercise("Bicep Curls",
                new String[]{"Biceps", "Forearms", "Beginner"},
                new ArrayList<>(List.of(
                        "Hold a dumbbell in each hand with palms facing forward.",
                        "Curl the weights towards your shoulders.",
                        "Lower back down to the starting position.")),
                "bicep-curls.png"
                ));

        exercises.add(createExercise("Triceps Dips",
                new String[]{"Triceps", "Shoulders", "Chest", "Intermediate"},
                new ArrayList<>(List.of(
                        "Grip parallel bars or a bench.",
                        "Lower your body by bending your elbows.",
                        "Push back up to the starting position.")),
                "tricep-dips.png"
                ));
    }

    @Override
    public List<Exercise> getAllExercises() {
        return Collections.unmodifiableList(exercises);
    }

    @Override
    public Exercise getExerciseByName(String name) {
        for (var exercise : exercises) {
            if (exercise.getName().equals(name)) {
                return exercise;
            }
        }
        return null; // Return null instead of incomplete code
    }

    public Exercise getExerciseByID(int id){
        Exercise result = null;

        for(int i=0; i<exercises.size() && result == null; i++){
            if(exercises.get(i).getID() == id){
                result = exercises.get(i);
            }
        }

        return result;
    }

    public List<Exercise> filterByQuery(String query){
        List<Exercise> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(exercises);
        } else {
            String lowerCaseQuery = query.toLowerCase();

            // go through all exercises
            for (Exercise exercise : exercises) {
                // check if the exercise name contains the query
                if (exercise.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(exercise);
                } else {
                    // this was recommended to change to something more efficient
                    // we would have to change how we store tags

                    // otherwise look through the exercise's tags for the query
                    for (Tag tag : exercise.getTags()) {
                        if (tag.getName().toLowerCase().contains(lowerCaseQuery)) {
                            filteredList.add(exercise);
                            break;
                        }
                    }
                }
            }
        }

        return filteredList;
    }
}
