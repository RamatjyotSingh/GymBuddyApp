package comp3350.gymbuddy.objects;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private static int static_id = 0;
    private final int id;
    private final String name;
    private final List<Tag> tags;
    private final ArrayList<String> instructions;
    private final String imagePath;

    public Exercise(String name, List<Tag> tags, ArrayList<String> instructions, String imagePath) {
        this.id = static_id++;
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = imagePath;
    }

    public Exercise(String name, List<Tag> tags, ArrayList<String> instructions) {
        this.id = static_id++;
        this.name = name;
        this.tags = tags;
        this.instructions = instructions;
        this.imagePath = null;
    }

    public int getID(){ return id; }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public String getImagePath() {
        return imagePath;
    }
}
