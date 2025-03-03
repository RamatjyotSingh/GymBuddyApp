package comp3350.gymbuddy.tests.objects;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;


public class ExerciseTest {
    @Test
    public void testExercise(){
        Exercise exercise;

        List<Tag> tagList = List.of(new Tag(Tag.TagType.MUSCLE_GROUP,"Chest", "#000","#fff"));
        List<String> instructions = List.of("Placeholder");

        exercise = new Exercise(0, "Push-up", tagList, instructions, "path",false, false);
        assertNotNull(exercise);
        assertEquals(0, exercise.getID());
        assertEquals("Push-up", exercise.getName());
        assertEquals(tagList, exercise.getTags());
        assertEquals(instructions, exercise.getInstructions());
        assertEquals("path", exercise.getImagePath());
    }
}
