package comp3350.gymbuddy.presentation.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.logic.ObjectStringer;
import comp3350.gymbuddy.objects.WorkoutItem;

public class WorkoutItemAdapter extends RecyclerView.Adapter<WorkoutItemAdapter.WorkoutViewHolder> {
    private final List<WorkoutItem> workoutItems;

    public WorkoutItemAdapter(List<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem item = workoutItems.get(position);
        holder.txtExerciseName.setText(item.getExercise().getName());

        ObjectStringer stringer = new ObjectStringer();
        String details = stringer.workoutBuilderItemString(item);

        holder.txtWorkoutDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return workoutItems.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName, txtWorkoutDetails;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
            txtWorkoutDetails = itemView.findViewById(R.id.txtWorkoutDetails);
        }
    }

    public void addWorkoutItem(WorkoutItem item) {
        workoutItems.add(item);
        notifyItemInserted(workoutItems.size() - 1);
    }

}
