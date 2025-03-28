package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.logic.WorkoutPlaybackController;

public class WorkoutPlaybackControllerTest {
    private WorkoutPlaybackController controller;
    private WorkoutPlaybackController.BeginWorkoutItemListener beginListener;
    private WorkoutPlaybackController.FinishWorkoutListener finishListener;
    private WorkoutProfile profile;
    private List<WorkoutItem> items;

    @Before
    public void setup() {
        //mock listeners
        beginListener = mock(WorkoutPlaybackController.BeginWorkoutItemListener.class);
        finishListener = mock(WorkoutPlaybackController.FinishWorkoutListener.class);

        //test data
        items = new ArrayList<>();
        profile = new WorkoutProfile("Test", "path", items);
        controller = new WorkoutPlaybackController(profile, beginListener, finishListener);
    }

    @Test
    public void testStartWorkout_WithItems() {
        items.add(new WorkoutItem(mock(Exercise.class), 3, 10, 30));

        controller.startWorkout();

        verify(beginListener).onBeginWorkoutItem(any(WorkoutItem.class));
    }

    @Test
    public void testProceed_ToFinish() {
        items.add(new WorkoutItem(mock(Exercise.class), 1, 1, 1));

        controller.startWorkout();
        controller.proceed(); //Should finish after one item

        verify(finishListener).onFinishedWorkout(any(WorkoutSession.class));
    }

    @Test
    public void testProceed_ToNextItem() {
        items.add(new WorkoutItem(mock(Exercise.class), 1, 1, 1));
        items.add(new WorkoutItem(mock(Exercise.class), 1, 1, 1));

        controller.startWorkout();
        controller.proceed();

        verify(beginListener, times(2)).onBeginWorkoutItem(any(WorkoutItem.class));
    }
}