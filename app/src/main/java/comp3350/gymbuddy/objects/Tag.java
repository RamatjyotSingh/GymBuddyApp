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
}
