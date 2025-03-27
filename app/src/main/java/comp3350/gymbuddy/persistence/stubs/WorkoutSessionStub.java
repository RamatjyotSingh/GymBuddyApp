package comp3350.gymbuddy.persistence.stubs;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Random;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.objects.WorkoutSession;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutSessionDB;
import comp3350.gymbuddy.persistence.util.WorkoutItemGenerator;

/**
 * WorkoutSessionStub simulates a database of workout sessions for testing purposes.
 */
public class WorkoutSessionStub implements IWorkoutSessionDB {

    // Constants for workout session generation.
    private static final int NUM_SESSIONS = 4;
    public static final int MIN_DURATION = 60; // Minimum session duration (1 min in seconds)
    public static final int MAX_DURATION = 2 * 60 * 60; // Maximum session duration (2 hrs in seconds)

    private final List<WorkoutSession> sessions;
    private int nextId; // Simulated auto-incrementing ID
    
    // Keep track of workout items by session ID for faster lookups
    private final Map<Integer, List<WorkoutItem>> sessionExercises;

    /**
     * Initializes the stub with a set number of randomly generated workout sessions.
     */
    public WorkoutSessionStub() {
        sessions = new ArrayList<>();
        sessionExercises = new HashMap<>();
        nextId = 0;

        // Generate mock workout sessions.
        for (int i = 0; i < NUM_SESSIONS; i++) {
            WorkoutSession session = createWorkoutSession();
            sessions.add(session);
            sessionExercises.put(session.getId(), new ArrayList<>(session.getWorkoutItems()));
        }
    }

    /**
     * Retrieves all workout sessions.
     * @return A list of workout sessions.
     */
    @Override
    public List<WorkoutSession> getAll() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Retrieves a workout session by its ID.
     * @param id The ID of the workout session.
     * @return The matching workout session, or null if not found.
     */
    @Override
    public WorkoutSession getWorkoutSessionByid(int id) {
        WorkoutSession result = null;

        // Search for the matching session ID.
        for (WorkoutSession session : sessions) {
            if (session.getId() == id) {
                result = session;
                break;
            }
        }

        return result;
    }

    /**
     * Creates a new randomly generated workout session.
     * @return A new WorkoutSession object.
     */
    private WorkoutSession createWorkoutSession() {
        int id = nextId;

        // Initialize a workout item generator for this session.
        var workoutItemGenerator = new WorkoutItemGenerator(id);

        // Retrieve a list of available workout profiles from the stub database.
        var workoutStub = new WorkoutStub();
        List<WorkoutProfile> profiles = workoutStub.getAll();

        // Generate a random session duration within the allowed range.
        Random rand = new Random();
        long duration = rand.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;

        // Get the current timestamp.
        Date now = new Date();

        // Determine session start and end times.
        long startTime = now.getTime();
        long endTime = now.getTime() + duration * 1000; // Convert seconds to milliseconds

        // Generate a list of workout items for the session.
        List<WorkoutItem> workoutItems = workoutItemGenerator.generate();

        // Assign a random workout profile to the session.
        WorkoutProfile profile = profiles.get(rand.nextInt(profiles.size()));

        // Increment the ID counter for the next session.
        nextId++;

        // Return the generated workout session.
        return new WorkoutSession(id, startTime, endTime, workoutItems, profile);
    }
    
    /**
     * Inserts a new workout session into the stub database.
     * 
     * @param session The workout session to insert.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean insertSession(WorkoutSession session) throws DBException {
        if (session == null) {
            return false;
        }
        
        // Check for duplicates
        for (WorkoutSession existingSession : sessions) {
            if (existingSession.getId() == session.getId()) {
                return false; // ID already exists
            }
        }
        
        // Add session to list
        sessions.add(session);
        
        // Store workout items by session ID
        sessionExercises.put(session.getId(), new ArrayList<>(session.getWorkoutItems()));
        
        // Update nextId if necessary
        if (session.getId() >= nextId) {
            nextId = session.getId() + 1;
        }
        
        return true;
    }
    
    /**
     * Adds an exercise to an existing workout session.
     * 
     * @param sessionId ID of the session.
     * @param item The workout item to add.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean addExerciseToSession(int sessionId, WorkoutItem item) throws DBException {
        // Find the session
        WorkoutSession session = getWorkoutSessionByid(sessionId);
        if (session == null || item == null) {
            return false;
        }
        
        // Get the current items for this session
        List<WorkoutItem> items = sessionExercises.get(sessionId);
        if (items == null) {
            items = new ArrayList<>();
            sessionExercises.put(sessionId, items);
        }
        
        // Check for duplicate exercise
        for (WorkoutItem existingItem : items) {
            if (existingItem.getExercise().getID() == item.getExercise().getID()) {
                return false; // Exercise already exists in session
            }
        }
        
        // Add the item
        items.add(item);
        
        // Update the session in the list with a new instance containing the updated items
        updateSessionInList(session, items);
        
        return true;
    }
    
    /**
     * Updates an existing workout session.
     * 
     * @param session The workout session with updated values.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean updateSession(WorkoutSession session) throws DBException {
        if (session == null) {
            return false;
        }
        
        // Find the index of the existing session
        int index = -1;
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == session.getId()) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            return false; // Session not found
        }
        
        // Replace with updated session
        sessions.set(index, session);
        
        // Update workout items
        sessionExercises.put(session.getId(), new ArrayList<>(session.getWorkoutItems()));
        
        return true;
    }
    
    /**
     * Updates just the end time of a workout session.
     * 
     * @param sessionId ID of the session to update.
     * @param endTime The new end time value.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean updateSessionEndTime(int sessionId, long endTime) throws DBException {
        // Find the session
        WorkoutSession existingSession = getWorkoutSessionByid(sessionId);
        if (existingSession == null) {
            return false;
        }
        
        // Create an updated session with the new end time
        WorkoutSession updatedSession = new WorkoutSession(
            existingSession.getId(),
            existingSession.getStartTime(),
            endTime,
            existingSession.getWorkoutItems(),
            existingSession.getWorkoutProfile()
        );
        
        // Update the session
        return updateSession(updatedSession);
    }
    
    /**
     * Deletes a workout session and all its associated items.
     * 
     * @param sessionId ID of the session to delete.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean deleteSession(int sessionId) throws DBException {
        // Find the index of the session
        int index = -1;
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == sessionId) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            return false; // Session not found
        }
        
        // Remove the session and its exercises
        sessions.remove(index);
        sessionExercises.remove(sessionId);
        
        return true;
    }
    
    /**
     * Removes an exercise from a workout session.
     * 
     * @param sessionId ID of the session.
     * @param exerciseId ID of the exercise to remove.
     * @return True if successful, false otherwise.
     * @throws DBException If an error occurs.
     */
    @Override
    public boolean removeExerciseFromSession(int sessionId, int exerciseId) throws DBException {
        // Find the session
        WorkoutSession session = getWorkoutSessionByid(sessionId);
        if (session == null) {
            return false;
        }
        
        // Get the items for this session
        List<WorkoutItem> items = sessionExercises.get(sessionId);
        if (items == null || items.isEmpty()) {
            return false;
        }
        
        // Find and remove the exercise
        boolean removed = false;
        List<WorkoutItem> updatedItems = new ArrayList<>();
        for (WorkoutItem item : items) {
            if (item.getExercise().getID() != exerciseId) {
                updatedItems.add(item);
            } else {
                removed = true;
            }
        }
        
        if (!removed) {
            return false; // Exercise not found
        }
        
        // Update the session items
        sessionExercises.put(sessionId, updatedItems);
        
        // Update the session in the list with a new instance containing the updated items
        updateSessionInList(session, updatedItems);
        
        return true;
    }
    
    /**
     * Searches for workout sessions by matching profile name or date.
     * 
     * @param query The search query string.
     * @return A list of matching workout sessions.
     * @throws DBException If an error occurs.
     */
    @Override
    @NonNull
    public List<WorkoutSession> search(String query) throws DBException {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        List<WorkoutSession> results = new ArrayList<>();
        
        for (WorkoutSession session : sessions) {
            // Check profile name
            String profileName = session.getWorkoutProfile().getName().toLowerCase();
            // Check date from session start time
            String sessionDate = new Date(session.getStartTime()).toString().toLowerCase();
            
            if (profileName.contains(lowerQuery) || sessionDate.contains(lowerQuery)) {
                results.add(session);
            }
        }
        
        return results;
    }
    
    /**
     * Gets all exercises for a specific workout session.
     * 
     * @param sessionId ID of the session.
     * @return List of workout items for the session.
     * @throws DBException If an error occurs.
     */
    @Override
    @NonNull
    public List<WorkoutItem> getExercisesForSession(int sessionId) throws DBException {
        List<WorkoutItem> items = sessionExercises.get(sessionId);
        return items != null ? Collections.unmodifiableList(items) : new ArrayList<>();
    }
    
    /**
     * Helper method to update a session in the sessions list with new workout items
     */
    private void updateSessionInList(WorkoutSession session, List<WorkoutItem> updatedItems) {
        int index = -1;
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == session.getId()) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            // Create a new session with updated items
            WorkoutSession updatedSession = new WorkoutSession(
                session.getId(),
                session.getStartTime(),
                session.getEndTime(),
                updatedItems,
                session.getWorkoutProfile()
            );
            
            // Replace the old session
            sessions.set(index, updatedSession);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
