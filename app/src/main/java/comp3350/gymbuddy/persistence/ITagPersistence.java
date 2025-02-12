package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.objects.Tag;
import java.util.List;

public interface ITagPersistence {
    Tag getTagByName(String name);
}
