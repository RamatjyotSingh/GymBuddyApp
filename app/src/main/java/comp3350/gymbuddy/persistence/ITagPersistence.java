package comp3350.gymbuddy.persistence;

import java.util.List;

import comp3350.gymbuddy.objects.Tag;

public interface ITagPersistence extends IPersistence{
    List<Tag> getAll();

    List<Tag> getTagsByExerciseID(int ExerciseID);
}
