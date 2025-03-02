package comp3350.gymbuddy.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class WorkoutProfileAdapter extends RecyclerView.Adapter<WorkoutProfileAdapter.WorkoutProfileViewHolder>{

    private final List<WorkoutProfile> workoutProfiles;

    public WorkoutProfileAdapter(List<WorkoutProfile> workoutProfiles) {
        this.workoutProfiles = workoutProfiles;
    }

    @NonNull
    @Override
    public WorkoutProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_profile, parent, false);
        return new WorkoutProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutProfileViewHolder holder, int position) {
        WorkoutProfile profile = workoutProfiles.get(position);
        holder.txtWorkoutName.setText(profile.getName());

        // display the number of exercises in the workout
        int exerciseCount = profile.getWorkoutItems().size();
        holder.txtWorkoutDetails.setText(exerciseCount + " exercises");
    }

    @Override
    public int getItemCount() {
        return workoutProfiles.size();
    }

    public static class WorkoutProfileViewHolder extends RecyclerView.ViewHolder {
        TextView txtWorkoutName, txtWorkoutDetails;

        public WorkoutProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutDetails = itemView.findViewById(R.id.txtWorkoutDetails);
        }
    }


}

