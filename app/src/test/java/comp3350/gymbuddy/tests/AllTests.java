package comp3350.gymbuddy.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.gymbuddy.tests.logic.ExerciseServiceTest;
import comp3350.gymbuddy.tests.objects.ExerciseTest;
import comp3350.gymbuddy.tests.objects.SessionItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutProfileTest;
import comp3350.gymbuddy.tests.objects.WorkoutSessionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // logic tests
        ExerciseServiceTest.class,

        // object tests
        ExerciseTest.class,
        SessionItemTest.class,
        WorkoutItemTest.class,
        WorkoutProfileTest.class,
        WorkoutSessionTest.class,

        // util tests
})

public class AllTests {
}
