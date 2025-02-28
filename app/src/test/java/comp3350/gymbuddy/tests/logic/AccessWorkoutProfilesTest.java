package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import comp3350.gymbuddy.logic.AccessWorkoutProfiles;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;

public class AccessWorkoutProfilesTest {
    private AccessWorkoutProfiles accessWorkoutProfiles;
    private IWorkoutProfilePersistence workoutProfilePersistence;

    @Before
    public void setup(){
        workoutProfilePersistence = mock(IWorkoutProfilePersistence.class);
        accessWorkoutProfiles = new AccessWorkoutProfiles(workoutProfilePersistence);
    }

    @Test
    public void testGetAllWorkoutProfiles(){
        final List<WorkoutProfile> workoutProfileList = new ArrayList<>();
        final List<WorkoutProfile> resultList;

        final WorkoutProfile profile = new WorkoutProfile("Test", "path", null);
        workoutProfileList.add(profile);

        when(workoutProfilePersistence.getAll()).thenReturn(workoutProfileList);

        resultList = accessWorkoutProfiles.getAll();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(profile, resultList.get(0));

        verify(workoutProfilePersistence).getAll();
    }
}
