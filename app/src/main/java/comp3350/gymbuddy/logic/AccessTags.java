package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.ITagPersistence;
import java.util.List;

public class AccessTags {
    private final ITagPersistence tagPersistence;

    public AccessTags(ITagPersistence tagPersistence) {
        this.tagPersistence = tagPersistence;
    }

    public List<Tag> getAllTags() {
        return tagPersistence.getAllTags();
    }

    public Tag getTagByName(String name) {
        return tagPersistence.getTagByName(name);
    }
}
