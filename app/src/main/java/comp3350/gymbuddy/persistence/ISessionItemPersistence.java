package comp3350.gymbuddy.persistence;

import java.util.List;

import comp3350.gymbuddy.objects.SessionItem;

public interface ISessionItemPersistence {
    List<SessionItem> getAllSessionItems();
}
