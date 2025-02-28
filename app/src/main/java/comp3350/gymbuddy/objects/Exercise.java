package comp3350.gymbuddy.objects;

import java.util.List;

public class Exercise {
    private final int id;
    private final String name;
    private final List<Tag> tags;
    private final List<String> instructions;
    private final String imagePath;
    private final boolean isTimeBased;
    private final boolean hasWeight;

    public Exercise(int id, String name, List<Tag> tags, List<String> instructions, String imagePath) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = imagePath;
        this.isTimeBased = false;
        this.hasWeight = false;
    }

    public int getID(){
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isTimeBased() {
        return isTimeBased;
    }

    public boolean hasWeight() {
        return hasWeight;
    }
}
