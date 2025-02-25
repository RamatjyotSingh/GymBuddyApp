package comp3350.gymbuddy.logic;

import java.util.List;

import comp3350.gymbuddy.persistence.IPersistence;

public abstract class Access {
    IPersistence persistence;

    public abstract List getAll();
}
