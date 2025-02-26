package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.objects.Tag.TagType;
import comp3350.gymbuddy.persistence.interfaces.ITagPersistence;

public class TagStub implements ITagPersistence {
    private final Map<String, Tag> tags;

    public TagStub() {
        tags = new HashMap<>();

        // Muscle Groups
        tags.put("Upper Body", new Tag(TagType.MUSCLE_GROUP, "Upper Body", "#1D4ED8", "#D1E8FF"));
        tags.put("Lower Body", new Tag(TagType.MUSCLE_GROUP, "Lower Body", "#6B21A8", "#E9D8FD"));
        tags.put("Core", new Tag(TagType.MUSCLE_GROUP, "Core", "#0D9488", "#B2F5EA"));
        tags.put("Full Body", new Tag(TagType.MUSCLE_GROUP, "Full Body", "#15803D", "#DCFCE7"));

        // Difficulty Levels
        tags.put("Beginner", new Tag(TagType.DIFFICULTY, "Beginner", "#10B981", "#DCFCE7"));
        tags.put("Intermediate", new Tag(TagType.DIFFICULTY, "Intermediate", "#F59E0B", "#FEF3C7"));
        tags.put("Advanced", new Tag(TagType.DIFFICULTY, "Advanced", "#DC2626", "#FEE2E2"));

        // Exercise Types
        tags.put("Cardio", new Tag(TagType.EXERCISE_TYPE, "Cardio", "#D97706", "#FFEDD5"));
        tags.put("Strength", new Tag(TagType.EXERCISE_TYPE, "Strength", "#DB2777", "#FFD6E8"));
        tags.put("Flexibility", new Tag(TagType.EXERCISE_TYPE, "Flexibility", "#059669", "#D1FAE5"));
        tags.put("Balance", new Tag(TagType.EXERCISE_TYPE, "Balance", "#4338CA", "#E0E7FF"));
        tags.put("Endurance", new Tag(TagType.EXERCISE_TYPE, "Endurance", "#DB2777", "#FFE4E6"));

        // Equipment
        tags.put("No Equipment", new Tag(TagType.EQUIPMENT, "No Equipment", "#1E3A8A", "#E0F2FE"));
        tags.put("Dumbbells", new Tag(TagType.EQUIPMENT, "Dumbbells", "#BE185D", "#FCE7F3"));
        tags.put("Resistance Bands", new Tag(TagType.EQUIPMENT, "Resistance Bands", "#D97706", "#FEF3C7"));
    }

    @Override
    public List<Tag> getAll() {
        List<Tag> tagList = new ArrayList<>();

        for (String key : tags.keySet()) {
            tagList.add(tags.get(key));
        }

        return tagList;
    }




    @Override
    public List<Tag> getTagsByExerciseID(int ExerciseID) {

        List<Tag> tagList = new ArrayList<>(tags.values());
        Collections.shuffle(tagList);  // Shuffle the list to randomize
        return tagList.subList(0, 5);  // Return the first 5 tags from the shuffled list
    }
}
