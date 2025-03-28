package comp3350.gymbuddy.tests.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite that runs all database integration tests together.
 * This allows you to run all database-related tests with a single command.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ExerciseDAOTest.class,
    WorkoutDAOTest.class,
    WorkoutSessionDAOTest.class
})
public class AllIntegrationTest {
    // This class remains empty, it's just a holder for the annotations
    // The @RunWith and @Suite.SuiteClasses annotations tell JUnit to run
    // all the test classes listed in the @Suite.SuiteClasses annotation
}