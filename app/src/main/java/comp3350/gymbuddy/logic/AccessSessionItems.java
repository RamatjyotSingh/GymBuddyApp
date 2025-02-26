package comp3350.gymbuddy.logic;

import java.util.Collections;
import java.util.List;

import comp3350.gymbuddy.application.Services;
import comp3350.gymbuddy.objects.SessionItem;
import comp3350.gymbuddy.persistence.ISessionItemPersistence;

public class AccessSessionItems extends Access {
    public AccessSessionItems(){
        this.persistence = Services.getSessionItemPersistence();
    }

    @Override
    public List<SessionItem> getAll(){
        return Collections.unmodifiableList(this.persistence.getAll());
    }

    public AccessSessionItems(ISessionItemPersistence sessionItemPersistence){
        this();
        this.persistence = sessionItemPersistence;
    }
}
