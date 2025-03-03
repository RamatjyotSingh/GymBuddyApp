package comp3350.gymbuddy.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutSession;


public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.WorkoutLogViewHolder>{

    private final List<WorkoutSession> workoutSessions;

    public WorkoutLogAdapter(List<WorkoutSession> workoutSessions){
        this.workoutSessions = workoutSessions;
    }

    @NonNull
    @Override
    public WorkoutLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new WorkoutLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutLogViewHolder holder, int position) {
        WorkoutSession session = workoutSessions.get(position);

        holder.date.setText(session.getDate());
        holder.duration.setText(session.getDurationString());
        holder.profile.setText(session.getWorkoutProfile().getName());
    }

    @Override
    public int getItemCount() {
        return workoutSessions.size();
    }


    public static class WorkoutLogViewHolder extends RecyclerView.ViewHolder{
        TextView date, duration, profile; // and any others?

        public WorkoutLogViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txtDate);
            duration = itemView.findViewById(R.id.txtDuration);
            profile = itemView.findViewById(R.id.txtProfileName);
        }
    }
}
