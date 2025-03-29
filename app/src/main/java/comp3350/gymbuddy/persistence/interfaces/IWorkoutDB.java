package comp3350.gymbuddy.persistence.interfaces;

import java.util.List;

import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;

public interface IWorkoutDB extends AutoCloseable {
    /**
     * Get all workout profiles.
     * @return A list of all workout profiles.
     * @throws DBException If an error occurs while accessing the database.
     */
    List<WorkoutProfile> getAll() throws DBException;
    
    /**
     * Get a workout profile by its ID.
     * @param id The ID of the workout profile.
     * @return The workout profile with the specified ID, or null if not found.
     * @throws DBException If an error occurs while accessing the database.
     */
    WorkoutProfile getWorkoutProfileById(int id) throws DBException;
    
    /**
     * Save a workout profile. If the profile already exists, update it.
     * @param profile The workout profile to save.
     * @return True if the operation was successful, false otherwise.
     * @throws DBException If an error occurs while accessing the database.
     */
    void saveWorkout(WorkoutProfile profile) throws DBException;
    
    /**
     * Delete a workout profile.
     * @param id The ID of the workout profile to delete.
     * @throws DBException If an error occurs while accessing the database.
     */
    void deleteWorkout(int id) throws DBException;
    
    /**
     * Search for workout profiles containing the given query in their name.
     * @param query The search query.
     * @return A list of workout profiles matching the query.
     * @throws DBException If an error occurs while accessing the database.
     */
    List<WorkoutProfile> search(String query) throws DBException;

    WorkoutProfile getWorkoutProfileByIdIncludingDeleted(int profileId);
}
