package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.objects.Set;
import comp3350.gymbuddy.persistence.ISetPersistence;
import comp3350.gymbuddy.persistence.stubs.SetStub;

public class SetService {
    // these persistence classes are singletons - only 1 instance of them for the lifetime of the application
    // meaning there will only be 1 database connection the entire time
    private ISetPersistence setPersistence;

    // Default constructor uses ExerciseStub
    public SetService() {
        if(this.setPersistence != null){
            this.setPersistence = new SetStub(); // Default stub implementation
        }
    }

    public SetService(ISetPersistence setPersistence) {
        if(this.setPersistence != null){
            this.setPersistence = setPersistence;
        }
    }

    public List<Set> getAllSetItems() {
        return this.setPersistence.getAllSets();
    }
}
