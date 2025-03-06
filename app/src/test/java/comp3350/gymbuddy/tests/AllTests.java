package comp3350.gymbuddy.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.gymbuddy.tests.logic.ExerciseManagerTest;
import comp3350.gymbuddy.tests.objects.ExerciseTest;
import comp3350.gymbuddy.tests.objects.WorkoutItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutProfileTest;
import comp3350.gymbuddy.tests.objects.WorkoutSessionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // logic tests
        ExerciseManagerTest.class,

        // object tests
        ExerciseTest.class,
        WorkoutItemTest.class,
        WorkoutItemTest.class,
        WorkoutProfileTest.class,
        WorkoutSessionTest.class,

        // util tests
})

public class AllTests {
}
