package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.AccessSessionItems;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.persistence.interfaces.ISessionItemPersistence;

public class AccessSessionItemsTest {
    private AccessSessionItems accessSessionItems;
    private ISessionItemPersistence sessionItemPersistence;

    @Before
    public void setup(){
        sessionItemPersistence = mock(ISessionItemPersistence.class);
        accessSessionItems = new AccessSessionItems(sessionItemPersistence);
    }

    @Test
    public void testGetAllSessionItems() {
        final List<SessionItem> sessionItemList = new ArrayList<>();
        final List<SessionItem> resultList;

        // Mock an Exercise object to avoid null pointer errors
        Exercise mockExercise = mock(Exercise.class);

        // Use a concrete subclass (RepBasedWorkoutItem)
        WorkoutItem workoutItem = new WorkoutItem(mockExercise, 3, 12, 50.0);

        // Now use this in the SessionItem
        SessionItem item = new SessionItem(workoutItem, 100, 15);
        sessionItemList.add(item);

        // Define mock behavior
        when(sessionItemPersistence.getAll()).thenReturn(sessionItemList);

        // Fetch session items
        resultList = accessSessionItems.getAll();

        // Assertions
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(item, resultList.get(0));

        verify(sessionItemPersistence).getAll();
    }

}
