package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.Tag;

public interface ITagPersistence {
    List<Tag> getAll();

    List<Tag> getTagsByExerciseID(int ExerciseID);
}
