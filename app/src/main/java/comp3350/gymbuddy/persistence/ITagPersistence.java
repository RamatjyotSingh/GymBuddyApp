package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.objects.Tag;

public interface ITagPersistence {
    Tag getTagByName(String name);
}
