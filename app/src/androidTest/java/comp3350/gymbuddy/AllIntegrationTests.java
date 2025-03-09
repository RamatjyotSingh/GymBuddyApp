package comp3350.gymbuddy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.gymbuddy.tests.logic.ExerciseManagerIntegrationTest;
import comp3350.gymbuddy.tests.logic.WorkoutManagerIntegrationTest;
import comp3350.gymbuddy.tests.logic.WorkoutSessionManagerIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // Logic integration tests
        ExerciseManagerIntegrationTest.class,
        WorkoutManagerIntegrationTest.class,
        WorkoutSessionManagerIntegrationTest.class,
})

public class AllIntegrationTests {
}
