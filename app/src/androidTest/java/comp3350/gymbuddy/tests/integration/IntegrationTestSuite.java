package comp3350.gymbuddy.tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ExerciseDBIntegrationTest.class,
    WorkoutDBIntegrationTest.class,
    WorkoutSessionDBIntegrationTest.class
})
public class IntegrationTestSuite {
    // Empty test suite
}