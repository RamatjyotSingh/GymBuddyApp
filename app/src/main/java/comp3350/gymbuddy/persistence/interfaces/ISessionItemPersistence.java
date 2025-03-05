package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.SessionItem;

public interface ISessionItemPersistence {
    void insertSessionItem(int workoutSessionId, SessionItem sessionItem);

    List<SessionItem> getAll();

    SessionItem getSessionItemById(int sessionItemId);

    List<SessionItem> getSessionItemsBySessionId(int sessionId);
}
