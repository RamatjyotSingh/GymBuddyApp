package comp3350.gymbuddy.persistence;

import comp3350.gymbuddy.persistence.engines.IPersistenceEngine;
import comp3350.gymbuddy.persistence.engines.StubEngine;

public class PersistenceManager {
    private static IPersistenceEngine persistenceEngine;

    public static synchronized IPersistenceEngine getEngine() {
        if (persistenceEngine == null) {
            persistenceEngine = new StubEngine();
        }
        return persistenceEngine;
    }
}
