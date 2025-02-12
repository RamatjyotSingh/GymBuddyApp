package comp3350.gymbuddy.logic;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.ITagPersistence;

import java.util.Collections;
import java.util.List;

public class AccessTags {
    final private ITagPersistence tagPersistence;

    public AccessTags() {
        tagPersistence = Services.getTagPersistence();
    }

    public List<Tag> getAllTags() {
        return Collections.unmodifiableList(tagPersistence.getAllTags());
    }
}
