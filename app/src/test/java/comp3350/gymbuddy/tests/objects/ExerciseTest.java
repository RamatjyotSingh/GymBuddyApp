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
        String instructions = "Placeholder";

        exercise = new Exercise(0, "Push-up", tagList, instructions, "path",false, false);
        assertNotNull(exercise);
        assertEquals(0, exercise.getID());
        assertEquals("Push-up", exercise.getName());
        assertEquals(tagList, exercise.getTags());
        assertEquals(instructions, exercise.getInstructions());
        assertEquals("path", exercise.getImagePath());
    }

    // Exception tests
    @Test
    public void testNegativeExerciseID() {
        Exercise exercise = new Exercise(-1, "Invalid", null, "instructions", "path", false, false);
        assertEquals(-1, exercise.getID()); // Verify it accepts negative IDs
    }

    @Test
    public void testNullExerciseName() {
        Exercise exercise = new Exercise(1, null, null, "instructions", "path", false, false);
        assertNull(exercise.getName()); // Verify it accepts null names
    }

    @Test
    public void testEmptyExerciseName() {
        Exercise exercise = new Exercise(1, "", null, "instructions", "path", false, false);
        assertEquals("", exercise.getName()); // Verify it accepts empty names
    }

    @Test
    public void testBlankExerciseName() {
        Exercise exercise = new Exercise(1, "   ", null, "instructions", "path", false, false);
        assertEquals("   ", exercise.getName()); // Verify it accepts blank names
    }

    @Test
    public void testLongExerciseName() {
        String longName = "X".repeat(101);
        Exercise exercise = new Exercise(1, longName, null, "instructions", "path", false, false);
    }

    @Test
    public void testNullImagePathAllowed() {
        Exercise exercise = new Exercise(1, "Exercise", null, "instructions", null, false, false);
        assertNull(exercise.getImagePath());
    }

    @Test
    public void testNullInstructionsAllowed() {
        Exercise exercise = new Exercise(1, "Exercise", null, null, "path", false, false);
        assertNull(exercise.getInstructions());
    }

    @Test
    public void testValidTimeBasedExercise() {
        Exercise exercise = new Exercise(1, "Plank", null, "Hold position", "path", true, false);
        assertTrue(exercise.isTimeBased());
        assertFalse(exercise.hasWeight());
    }

    @Test
    public void testValidWeightBasedExercise() {
        Exercise exercise = new Exercise(1, "Bench Press", null, "Lift weight", "path", false, true);
        assertFalse(exercise.isTimeBased());
        assertTrue(exercise.hasWeight());
    }

}
