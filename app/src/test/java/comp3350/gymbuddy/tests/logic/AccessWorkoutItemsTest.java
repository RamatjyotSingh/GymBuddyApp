package comp3350.gymbuddy.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.logic.AccessWorkoutItems;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.persistence.IWorkoutItemPersistence;

public class AccessWorkoutItemsTest {
    private AccessWorkoutItems accessWorkoutItems;
    private IWorkoutItemPersistence workoutItemPersistence;

    @Before
    public void setup(){
        workoutItemPersistence = mock(IWorkoutItemPersistence.class);
        accessWorkoutItems = new AccessWorkoutItems(workoutItemPersistence);
    }

    @Test
    public void testGetAllWorkoutItems(){
        final List<WorkoutItem> workoutItemList = new ArrayList<>();
        final List<WorkoutItem> resultList;

        final WorkoutItem workoutItem = new WorkoutItem(null, 10);
        workoutItemList.add(workoutItem);
        when(workoutItemPersistence.getAll()).thenReturn(workoutItemList);

        resultList = accessWorkoutItems.getAll();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(workoutItem, resultList.get(0));

        verify(workoutItemPersistence).getAll();
    }
}
