package comp3350.gymbuddy.objects;

public class Tag {
    private final String name;
    private final String color;

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isDifficultyTag() {
        return name.equals("Beginner") || name.equals("Intermediate") || name.equals("Advanced");
    }

    public String getDifficulty() {
        return isDifficultyTag() ? name : "";
    }

    public String getMusclesWorked() {
        return isDifficultyTag() ? "" : name; // Assuming non-difficulty tags are muscle groups
    }
}
