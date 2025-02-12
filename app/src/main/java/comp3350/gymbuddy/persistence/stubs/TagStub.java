package comp3350.gymbuddy.persistence.stubs;

import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.ITagPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagStub implements ITagPersistence {
    private final Map<String, Tag> tags;

    public TagStub() {
        tags = new HashMap<>();

        // Add predefined tags (name -> Tag object)
        tags.put("Chest", new Tag("Chest", "red"));
        tags.put("Triceps", new Tag("Triceps", "red"));
        tags.put("Shoulders", new Tag("Shoulders", "orange"));
        tags.put("Core", new Tag("Core", "yellow"));
        tags.put("Quadriceps", new Tag("Quadriceps", "blue"));
        tags.put("Hamstrings", new Tag("Hamstrings", "blue"));
        tags.put("Glutes", new Tag("Glutes", "purple"));
        tags.put("Lower Back", new Tag("Lower Back", "brown"));
        tags.put("Back", new Tag("Back", "green"));
        tags.put("Biceps", new Tag("Biceps", "cyan"));
        tags.put("Forearms", new Tag("Forearms", "pink"));
        tags.put("Calves", new Tag("Calves", "gray"));
        tags.put("Balance", new Tag("Balance", "black"));

        // Difficulty levels as tags
        tags.put("Low", new Tag("Low", "lightgray"));
        tags.put("Medium", new Tag("Medium", "blue"));
        tags.put("High", new Tag("High", "red"));
    }

    @Override
    public Tag getTagByName(String name) {
        return tags.getOrDefault(name, new Tag(name, "black")); // Default to black if not found
    }
}
