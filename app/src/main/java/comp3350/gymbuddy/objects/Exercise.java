package comp3350.gymbuddy.objects;

import java.util.List;

public class Exercise {
    private final String name;
    private final List<Tag> tags;
    private final String instructions;
    private final String imagePath;

    public Exercise(String name, List<Tag> tags, String instructions, String imagePath) {
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = imagePath;
    }

    public Exercise(String name, List<Tag> tags, String instructions) {
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = null;
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

    public String getImagePath() {
        return imagePath;
    }
}
