package comp3350.gymbuddy.objects;

import java.util.List;

public class Exercise {
    private final String name;
    private final List<Tag> tags;
    private final String instructions;

    public Exercise(String name, List<Tag> tags, String instructions) {
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getInstructions() {
        return instructions;
    }
}
