package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.util.InputValidator;
import comp3350.gymbuddy.logic.util.ValidationMessages;
import comp3350.gymbuddy.logic.exception.InvalidInputException;
import comp3350.gymbuddy.logic.exception.InvalidNameException;
import comp3350.gymbuddy.logic.exception.InvalidRepsException;
import comp3350.gymbuddy.logic.exception.InvalidSetsException;
import comp3350.gymbuddy.logic.exception.InvalidTimeException;
import comp3350.gymbuddy.logic.exception.InvalidWeightException;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;


@RunWith(Suite.class)
@Suite.SuiteClasses({InputValidatorTest.TestNewWorkoutProfile.class, InputValidatorTest.TestNewWorkoutItem.class})
public class InputValidatorTest {
    public static class TestNewWorkoutProfile{
        private InputValidator inputValidator;
        private String name;
        private String iconPath;
        private List<WorkoutItem> workoutItems;

        @Before
        public void setup(){
            inputValidator = new InputValidator();

            // Setup default workout profile to be validated
            name = "Profile 1";
            iconPath = "path";
            workoutItems = new ArrayList<>();
            Exercise exercise = new Exercise(0, "Push-up", null, null, null, false, false);
            workoutItems.add(new WorkoutItem(exercise, 1, 1));
        }

        @Test
        public void testNewWorkoutProfile(){
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
            name = "";

            newWorkoutProfileCatchException(new InvalidNameException(ValidationMessages.invalidNameExceptionMessage));
        }

        @Test
        public void testNewWorkoutProfileInvalidWorkoutItems(){
            workoutItems = new ArrayList<>();

            newWorkoutProfileCatchException(new InvalidInputException(ValidationMessages.invalidInputExceptionMessage));
        }

        @Test
        public void testNewWorkoutProfileNullName(){
            name = null;

            newWorkoutProfileCatchException(new InvalidNameException(ValidationMessages.invalidNameExceptionMessage));
        }

        @Test
        public void testNewWorkoutProfileNullWorkoutItems(){
            workoutItems = null;

            newWorkoutProfileCatchException(new InvalidInputException(ValidationMessages.invalidInputExceptionMessage));
        }

    }

    public static class TestNewWorkoutItem{
        private InputValidator inputValidator;
        private Exercise exercise;
        int sets;
        int reps;
        double weight;
        double time;

        @Before
        public void setup(){
            inputValidator = new InputValidator();

            // Setup default workout item to be validated
            exercise = new Exercise(0, "Push-up", null, null, null, false, false);
            sets = 1;
            reps = 1;
            weight = 1.0;
            time = 1.0;
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
            reps = 0;
            weight = 0.0;

            assertNewWorkoutItem(sets, reps, weight, time);
        }

        @Test
        public void testNewWorkoutItemRepBasedNoWeight(){
            weight = 0.0;
            time = 0.0;

            assertNewWorkoutItem(sets, reps, weight, time);
        }

        @Test
        public void testNewWorkoutItemRepBasedHasWeight(){
            exercise = new Exercise(0, "Push-up", null, null, null, false, true);
            time = 0.0;

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
                assertEquals(ValidationMessages.invalidInputExceptionMessage, e.getMessage());
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

            newWorkoutItemCatchException(setsField, ""+1, ""+1.0, ""+1.0, new InvalidSetsException(ValidationMessages.integerFormatExceptionMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidSetsValue(){
            String setsField = "-10";

            newWorkoutItemCatchException(setsField, ""+1, ""+1.0, ""+1.0, new InvalidSetsException(ValidationMessages.invalidNonzeroValueMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidTimeFormat(){
            exercise = new Exercise(0, "Push-up", null, null, null, true, false);
            String timeField = "///";

            newWorkoutItemCatchException(""+1, ""+1, ""+1.0, timeField, new InvalidTimeException(ValidationMessages.doubleFormatExceptionMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidTimeValue(){
            exercise = new Exercise(0, "Push-up", null, null, null, true, false);
            String timeField = "-10";

            newWorkoutItemCatchException(""+1, ""+1, ""+1.0, timeField, new InvalidTimeException(ValidationMessages.invalidNonnegativeValueMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidRepsFormat(){
            String repsField = "///";

            newWorkoutItemCatchException(""+1, repsField, ""+1.0, ""+1.0, new InvalidRepsException(ValidationMessages.integerFormatExceptionMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidRepsValue(){
            String repsField = "-10";

            newWorkoutItemCatchException(""+1, repsField, ""+1.0, ""+1.0, new InvalidRepsException(ValidationMessages.invalidNonzeroValueMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidWeightFormat(){
            exercise = new Exercise(0, "Push-up", null, null, null, false, true);
            String weightField = "///";

            newWorkoutItemCatchException(""+1, ""+1, weightField, ""+1.0, new InvalidWeightException(ValidationMessages.doubleFormatExceptionMessage));
        }

        @Test
        public void testNewWorkoutItemInvalidWeightValue(){
            exercise = new Exercise(0, "Push-up", null, null, null, false, true);
            String weightField = "-10";

            newWorkoutItemCatchException(""+1, ""+1, weightField, ""+1.0, new InvalidWeightException(ValidationMessages.invalidNonnegativeValueMessage));
        }
    }
}
