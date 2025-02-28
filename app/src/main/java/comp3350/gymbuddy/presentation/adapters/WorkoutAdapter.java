package comp3350.gymbuddy.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.RepBasedWorkoutItem;
import comp3350.gymbuddy.objects.TimeBasedWorkoutItem;
import comp3350.gymbuddy.objects.WorkoutItem;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private final List<WorkoutItem> workoutItems;

    public WorkoutAdapter(List<WorkoutItem> workoutItems) {
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

        String details = "";

        if (item instanceof RepBasedWorkoutItem) {
            var repItem = (RepBasedWorkoutItem)item;
            details = repItem.getSets() + " sets x " + repItem.getReps() + " reps";

            if (repItem.getWeight() > 0) {
                details += " | " + repItem.getWeight() + " kg";
            }
        }
        else if (item instanceof TimeBasedWorkoutItem) {
            var timeItem = (TimeBasedWorkoutItem)item;
            details = timeItem.getTime() + " sec";
        }

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
            txtExerciseName = itemView.findViewById(R.id.txtDate);
            txtWorkoutDetails = itemView.findViewById(R.id.txtDuration);
        }
    }

    public void addWorkoutItem(WorkoutItem item) {
        workoutItems.add(item);
        notifyItemInserted(workoutItems.size() - 1);
    }
}
