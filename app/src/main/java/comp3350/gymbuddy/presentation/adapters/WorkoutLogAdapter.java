package comp3350.gymbuddy.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutSession;


public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.WorkoutLogViewHolder>{

    public interface WorkoutLogOnClickListener{
        void openWorkoutLogDetail(WorkoutSession workoutSession);
    }

    private List<WorkoutSession> workoutSessions;
    private final WorkoutLogOnClickListener clickListener;

    public WorkoutLogAdapter(List<WorkoutSession> workoutSessions, WorkoutLogOnClickListener clickListener){
        this.workoutSessions = new ArrayList<>(workoutSessions);
        this.clickListener = clickListener;
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

        holder.container.setOnClickListener(v -> clickListener.openWorkoutLogDetail(session));
    }

    @Override
    public int getItemCount() {
        return workoutSessions.size();
    }


    public void setWorkoutSessions(List<WorkoutSession> sessions){
        int oldSize = workoutSessions.size();
        workoutSessions = new ArrayList<>(sessions);
        notifyItemRangeChanged(0, oldSize);
    }


    public static class WorkoutLogViewHolder extends RecyclerView.ViewHolder{
        CardView container;
        TextView date, duration, profile;

        public WorkoutLogViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.logItemContainer);
            date = itemView.findViewById(R.id.txtDate);
            duration = itemView.findViewById(R.id.txtDuration);
            profile = itemView.findViewById(R.id.txtProfileName);
        }
    }
}
