package comp3350.gymbuddy;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.persistence.stubs.ExerciseStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ExerciseTest {
    private ExerciseStub exerciseStub;

    @Before
    public void setUp() {
        exerciseStub = new ExerciseStub(); // Initialize before each test
    }

    @Test
    public void testExerciseListNotEmpty() {
        List<Exercise> exercises = exerciseStub.getAllExercises();
        assertNotNull(exercises);
        assertFalse(exercises.isEmpty());
    }

    @Test
    public void testExerciseHasName() {
        List<Exercise> exercises = exerciseStub.getAllExercises();
        for (Exercise exercise : exercises) {
            assertNotNull(exercise.getName());
            assertFalse(exercise.getName().isEmpty());
        }
    }
}

