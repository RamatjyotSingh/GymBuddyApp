package comp3350.gymbuddy.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.gymbuddy.tests.logic.AccessExercisesTest;
import comp3350.gymbuddy.tests.logic.AccessSessionItemsTest;
import comp3350.gymbuddy.tests.logic.AccessWorkoutItemsTest;
import comp3350.gymbuddy.tests.objects.ExerciseTest;
import comp3350.gymbuddy.tests.objects.RepBasedSessionItemTest;
import comp3350.gymbuddy.tests.objects.RepBasedWorkoutItemTest;
import comp3350.gymbuddy.tests.objects.TimeBasedSessionItemTest;
import comp3350.gymbuddy.tests.objects.TimeBasedWorkoutItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutProfileTest;
import comp3350.gymbuddy.tests.objects.WorkoutSessionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // logic tests
        AccessExercisesTest.class,
        AccessSessionItemsTest.class,
        AccessWorkoutItemsTest.class,

        // object tests
        ExerciseTest.class,
        RepBasedSessionItemTest.class,
        RepBasedWorkoutItemTest.class,
        TimeBasedSessionItemTest.class,
        TimeBasedWorkoutItemTest.class,
        WorkoutItemTest.class,
        WorkoutProfileTest.class,
        WorkoutSessionTest.class,

        // util tests
})

public class AllTests {
}
