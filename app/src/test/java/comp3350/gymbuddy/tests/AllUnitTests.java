package comp3350.gymbuddy.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.gymbuddy.tests.logic.ApplicationServiceTest;
import comp3350.gymbuddy.tests.logic.ConfigLoaderTest;
import comp3350.gymbuddy.tests.logic.ExerciseManagerTest;
import comp3350.gymbuddy.tests.logic.InputValidatorTest;
import comp3350.gymbuddy.tests.logic.StringFormatterTest;
import comp3350.gymbuddy.tests.logic.WorkoutManagerTest;
import comp3350.gymbuddy.tests.logic.WorkoutPlaybackControllerTest;
import comp3350.gymbuddy.tests.logic.WorkoutSessionManagerTest;
import comp3350.gymbuddy.tests.objects.ExerciseTest;
import comp3350.gymbuddy.tests.objects.WorkoutItemTest;
import comp3350.gymbuddy.tests.objects.WorkoutProfileTest;
import comp3350.gymbuddy.tests.objects.WorkoutSessionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // logic unit tests
        ApplicationServiceTest.class,
        ConfigLoaderTest.class,
        ExerciseManagerTest.class,
        InputValidatorTest.class,
        StringFormatterTest.class,
        WorkoutManagerTest.class,
        WorkoutPlaybackControllerTest.class,
        WorkoutSessionManagerTest.class,

        // object tests
        ExerciseTest.class,
        WorkoutItemTest.class,
        WorkoutItemTest.class,
        WorkoutProfileTest.class,
        WorkoutSessionTest.class,
})

public class AllUnitTests {
}
