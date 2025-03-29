package comp3350.gymbuddy.persistence.interfaces;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.exception.DBException;

import java.util.List;

public interface IExerciseDAO {
    List<Exercise> getAll() throws DBException;

    Exercise getExerciseByID(int id) throws DBException;
}
