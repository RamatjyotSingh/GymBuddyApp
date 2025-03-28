package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import comp3350.gymbuddy.logic.ApplicationService;
import comp3350.gymbuddy.logic.exception.ApplicationInitException;
import comp3350.gymbuddy.logic.util.ConfigLoader;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {
    @Mock private ConfigLoader mockConfig;

    private ApplicationService service;

    @Before
    public void setUp() {
        //Reset singleton instance before each test
        ApplicationService.resetInstanceForTesting();

        when(mockConfig.getScriptPath()).thenReturn("script/path");
        when(mockConfig.getConfigPath()).thenReturn("config/path");

        service = ApplicationService.getInstance();
    }

    @After
    public void tearDown() {
        if (service != null) {
            service.close();
        }
    }

    @Test
    public void testGetInstance_ReturnsSameInstance() {
        ApplicationService instance1 = ApplicationService.getInstance();
        ApplicationService instance2 = ApplicationService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test(expected = ApplicationInitException.class)
    public void testGetExerciseManager_BeforeInitialization() {
        service.getExerciseManager();
    }

    @Test(expected = ApplicationInitException.class)
    public void testGetWorkoutManager_BeforeInitialization() {
        service.getWorkoutManager();
    }

    @Test(expected = ApplicationInitException.class)
    public void testGetWorkoutSessionManager_BeforeInitialization() {
        service.getWorkoutSessionManager();
    }

    @Test
    public void testReinitialize_AfterClose() throws Exception {
        service.initialize(mockConfig);
        service.close();
        service.initialize(mockConfig);

        assertTrue(service.isInitialized());
        assertFalse(service.isClosed());
    }

    @Test(expected = ApplicationInitException.class)
    public void testGetManager_AfterClose() throws Exception {
        service.initialize(mockConfig);
        service.close();
        service.getExerciseManager();
    }
}