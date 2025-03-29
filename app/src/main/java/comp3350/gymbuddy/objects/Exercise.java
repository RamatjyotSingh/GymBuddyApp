package comp3350.gymbuddy.objects;

import java.util.List;

public class Exercise {
    private final int id;
    private final String name;
    private final List<Tag> tags;
    private final String instructions;
    private final String imagePath;
    private final boolean isTimeBased;
    private final boolean hasWeight;

    public Exercise(int id, String name, List<Tag> tags, String instructions, String imagePath, boolean isTimeBased, boolean hasWeight) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = imagePath;
        this.isTimeBased = isTimeBased;
        this.hasWeight = hasWeight;
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

    public String getInstructions() {
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
