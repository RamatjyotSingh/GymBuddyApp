package comp3350.gymbuddy.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.WorkoutItem;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutDB;

public class WorkoutStub implements IWorkoutDB {
    private final List<WorkoutProfile> profiles;
    private int nextId;
    private final Map<Integer, List<WorkoutItem>> profileItems;
    
    public WorkoutStub() {
        profiles = new ArrayList<>();
        profileItems = new HashMap<>();
        nextId = 1;
        
        // Add some sample profiles
        addSampleProfiles();
    }
    
    private void addSampleProfiles() {
        ExerciseStub exerciseStub = new ExerciseStub();
        
        // Create some sample workout items
        List<WorkoutItem> items1 = new ArrayList<>();
        List<WorkoutItem> items2 = new ArrayList<>();
        
        // Get sample exercises
        Exercise pushUp = exerciseStub.getExerciseByID(1);
        Exercise squat = exerciseStub.getExerciseByID(2);
        Exercise plank = exerciseStub.getExerciseByID(3);
        
        if (pushUp != null && squat != null && plank != null) {
            items1.add(new WorkoutItem(pushUp, 3, 10, 0));
            items1.add(new WorkoutItem(squat, 3, 12, 40));
            
            items2.add(new WorkoutItem(pushUp, 4, 15, 0));
            items2.add(new WorkoutItem(plank, 3, 0, 0, 60));
        }
        
        WorkoutProfile profile1 = new WorkoutProfile(nextId++, "Full Body Workout", "workout_icon.png", items1);
        WorkoutProfile profile2 = new WorkoutProfile(nextId++, "Upper Body Focus", "upper_body_icon.png", items2);
        
        profiles.add(profile1);
        profiles.add(profile2);
        
        // Store items by profile ID
        profileItems.put(profile1.getId(), new ArrayList<>(items1));
        profileItems.put(profile2.getId(), new ArrayList<>(items2));
    }
    
    @Override
    public List<WorkoutProfile> getAll() throws DBException {
        return Collections.unmodifiableList(profiles);
    }
    
    @Override
    public WorkoutProfile getWorkoutProfileById(int id) throws DBException {
        for (WorkoutProfile profile : profiles) {
            if (profile.getId() == id) {
                // Get stored items for this profile
                List<WorkoutItem> items = profileItems.get(id);
                if (items == null) items = new ArrayList<>();
                
                // Return a new profile instance with the stored items
                return new WorkoutProfile(
                    profile.getId(), 
                    profile.getName(), 
                    profile.getIconPath(), 
                    new ArrayList<>(items)
                );
            }
        }
        return null;
    }
    
    @Override
    public boolean saveWorkout(WorkoutProfile profile) throws DBException {
        if (profile == null) {
            return false;
        }
        
        // Check if profile exists by name
        WorkoutProfile existingProfile = null;
        for (WorkoutProfile p : profiles) {
            if (p.getName().equals(profile.getName())) {
                existingProfile = p;
                break;
            }
        }
        
        if (existingProfile == null) {
            // Create new profile with a generated ID
            WorkoutProfile newProfile = new WorkoutProfile(
                nextId++, 
                profile.getName(), 
                profile.getIconPath(),
                profile.getWorkoutItems()
            );
            
            profiles.add(newProfile);
            
            // Store items by profile ID
            if (profile.getWorkoutItems() != null) {
                profileItems.put(newProfile.getId(), new ArrayList<>(profile.getWorkoutItems()));
            } else {
                profileItems.put(newProfile.getId(), new ArrayList<>());
            }
            
            return true;
        } else {
            // Update existing profile
            int index = profiles.indexOf(existingProfile);
            
            WorkoutProfile updatedProfile = new WorkoutProfile(
                existingProfile.getId(),
                profile.getName(),
                profile.getIconPath(),
                profile.getWorkoutItems() 
            );
            
            profiles.set(index, updatedProfile);
            
            // Update stored items
            if (profile.getWorkoutItems() != null) {
                profileItems.put(existingProfile.getId(), new ArrayList<>(profile.getWorkoutItems()));
            } else {
                profileItems.put(existingProfile.getId(), new ArrayList<>());
            }
            
            return true;
        }
    }
    
    @Override
    public boolean deleteWorkout(int id) throws DBException {
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getId() == id) {
                profiles.remove(i);
                profileItems.remove(id);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<WorkoutProfile> search(String query) throws DBException {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        List<WorkoutProfile> results = new ArrayList<>();
        
        for (WorkoutProfile profile : profiles) {
            if (profile.getName().toLowerCase().contains(lowerQuery)) {
                results.add(profile);
            }
        }
        
        return results;
    }
}
