package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.InputValidator;
import comp3350.gymbuddy.logic.LogicConfig;
import comp3350.gymbuddy.logic.exception.InvalidInputException;
import comp3350.gymbuddy.logic.exception.InvalidNameException;
import comp3350.gymbuddy.logic.exception.InvalidRepsException;
import comp3350.gymbuddy.logic.exception.InvalidSetsException;
import comp3350.gymbuddy.logic.exception.InvalidTimeException;
import comp3350.gymbuddy.logic.exception.InvalidWeightException;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class InputValidatorTest {
    private InputValidator inputValidator;
    private String name;
    private String iconPath;
    private List<WorkoutItem> workoutItems;
    private Exercise exercise;

    @Before
    public void setup(){
        inputValidator = new InputValidator();

        // Set common parameters
        exercise = new Exercise(0, "Push-up", null, null, null, false, false);
    }

    private void setDefaultWorkoutProfile(){
        // Setup default workout profile to be validated
        name = "Profile 1";
        iconPath = "path";
        workoutItems = new ArrayList<>();
        workoutItems.add(new WorkoutItem(exercise, 1, 1));
    }

    @Test
    public void testNewWorkoutProfile(){
        setDefaultWorkoutProfile();
        WorkoutProfile result = inputValidator.newWorkoutProfile(name, iconPath, workoutItems);

        assertEquals(name, result.getName());
        assertEquals(iconPath, result.getIconPath());
        assertEquals(workoutItems, result.getWorkoutItems());
    }

    private void newWorkoutProfileCatchException(Exception expectedException){
        try{
            inputValidator.newWorkoutProfile(name, iconPath, workoutItems);
            fail("No exception thrown.");
        }
        catch(Exception e){
            assertEquals("Incorrect exception thrown: " + e.getClass(), expectedException.getClass(), e.getClass());
            assertEquals("Incorrect exception message: " + e.getMessage(), expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testNewWorkoutProfileInvalidName(){
        setDefaultWorkoutProfile();
        name = "";

        newWorkoutProfileCatchException(new InvalidNameException(LogicConfig.invalidNameExceptionMessage));
    }

    @Test
    public void testNewWorkoutProfileInvalidWorkoutItems(){
        setDefaultWorkoutProfile();
        workoutItems = new ArrayList<>();

        newWorkoutProfileCatchException(new InvalidInputException(LogicConfig.invalidInputExceptionMessage));
    }

    @Test
    public void testNewWorkoutProfileNullName(){
        setDefaultWorkoutProfile();
        name = null;

        newWorkoutProfileCatchException(new InvalidNameException(LogicConfig.invalidNameExceptionMessage));
    }

    @Test
    public void testNewWorkoutProfileNullWorkoutItems(){
        setDefaultWorkoutProfile();
        workoutItems = null;

        newWorkoutProfileCatchException(new InvalidInputException(LogicConfig.invalidInputExceptionMessage));
    }

    private void assertNewWorkoutItem(int sets, int reps, double weight, double time){
        WorkoutItem result = inputValidator.newWorkoutItem(exercise, ""+sets, ""+reps, ""+weight, ""+time);

        assertEquals(exercise, result.getExercise());
        assertEquals(sets, result.getSets());
        assertEquals(reps, result.getReps());
        assertEquals(weight, result.getWeight(), 0);
        assertEquals(time, result.getTime(), 0);
    }

    @Test
    public void testNewWorkoutItemTimeBased(){
        exercise = new Exercise(0, "Push-up", null, null, null, true, false);
        int sets = 1;
        int reps = 0;
        double weight = 0.0;
        double time = 1.0;

        assertNewWorkoutItem(sets, reps, weight, time);
    }

    @Test
    public void testNewWorkoutItemRepBasedNoWeight(){
        exercise = new Exercise(0, "Push-up", null, null, null, false, false);
        int sets = 1;
        int reps = 1;
        double weight = 0.0;
        double time = 0.0;

        assertNewWorkoutItem(sets, reps, weight, time);
    }

    @Test
    public void testNewWorkoutItemRepBasedHasWeight(){
        exercise = new Exercise(0, "Push-up", null, null, null, false, true);
        int sets = 1;
        int reps = 1;
        double weight = 1.0;
        double time = 0.0;

        assertNewWorkoutItem(sets, reps, weight, time);
    }

    @Test
    public void testNewWorkoutItemNullExercise(){
        exercise = null;

        try{
            inputValidator.newWorkoutItem(exercise, ""+1, ""+1, ""+1.0, ""+1.0);
            fail("InvalidInputException not thrown.");
        }
        catch(InvalidInputException e){
            assertEquals(LogicConfig.invalidInputExceptionMessage, e.getMessage());
        }
        catch(Exception e){
            fail("Incorrect exception thrown: " + e.getClass());
        }
    }

    private void newWorkoutItemCatchException(String setsField, String repsField, String weightField, String timeField, Exception expectedException){
        try{
            inputValidator.newWorkoutItem(exercise, setsField, repsField, weightField, timeField);
            fail("No exception thrown.");
        }
        catch(Exception e){
            assertEquals("Incorrect exception thrown: " + e.getClass(), expectedException.getClass(), e.getClass());
            assertEquals("Incorrect exception message: " + e.getMessage(), expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testNewWorkoutItemInvalidSetsFormat(){
        String setsField = "///";

        newWorkoutItemCatchException(setsField, ""+1, ""+1.0, ""+1.0, new InvalidSetsException(LogicConfig.integerFormatExceptionMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidSetsValue(){
        String setsField = "-10";

        newWorkoutItemCatchException(setsField, ""+1, ""+1.0, ""+1.0, new InvalidSetsException(LogicConfig.invalidNonzeroValueMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidTimeFormat(){
        exercise = new Exercise(0, "Push-up", null, null, null, true, false);
        String timeField = "///";

        newWorkoutItemCatchException(""+1, ""+1, ""+1.0, timeField, new InvalidTimeException(LogicConfig.doubleFormatExceptionMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidTimeValue(){
        exercise = new Exercise(0, "Push-up", null, null, null, true, false);
        String timeField = "-10";

        newWorkoutItemCatchException(""+1, ""+1, ""+1.0, timeField, new InvalidTimeException(LogicConfig.invalidNonnegativeValueMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidRepsFormat(){
        String repsField = "///";

        newWorkoutItemCatchException(""+1, repsField, ""+1.0, ""+1.0, new InvalidRepsException(LogicConfig.integerFormatExceptionMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidRepsValue(){
        String repsField = "-10";

        newWorkoutItemCatchException(""+1, repsField, ""+1.0, ""+1.0, new InvalidRepsException(LogicConfig.invalidNonzeroValueMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidWeightFormat(){
        exercise = new Exercise(0, "Push-up", null, null, null, false, true);
        String weightField = "///";

        newWorkoutItemCatchException(""+1, ""+1, weightField, ""+1.0, new InvalidWeightException(LogicConfig.doubleFormatExceptionMessage));
    }

    @Test
    public void testNewWorkoutItemInvalidWeightValue(){
        exercise = new Exercise(0, "Push-up", null, null, null, false, true);
        String weightField = "-10";

        newWorkoutItemCatchException(""+1, ""+1, weightField, ""+1.0, new InvalidWeightException(LogicConfig.invalidNonnegativeValueMessage));
    }
}
