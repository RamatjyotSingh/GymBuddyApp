package comp3350.gymbuddy.tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    
    // Run the database verification test first
    DatabaseSetupTest.class,
    
    // Then run the other tests
    ExerciseDBIntegrationTest.class,
    WorkoutDBIntegrationTest.class,
    WorkoutSessionDBIntegrationTest.class
})
public class IntegrationTestSuite {
    // Empty test suite
}