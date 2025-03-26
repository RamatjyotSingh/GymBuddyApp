package comp3350.gymbuddy.presentation.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutItem;

public class WorkoutItemAdapter extends RecyclerView.Adapter<WorkoutItemAdapter.WorkoutViewHolder> {
    private final List<WorkoutItem> workoutItems;
    private ItemDeleteListener deleteListener;
    private boolean showDeleteButtons = false; // Default to not showing delete buttons

    // Interface for delete event callbacks
    public interface ItemDeleteListener {
        void onItemDelete(int position);
    }

    public WorkoutItemAdapter(List<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }

    public void setItemDeleteListener(ItemDeleteListener listener) {
        this.deleteListener = listener;
    }
    
    // New method to control delete button visibility
    public void setShowDeleteButtons(boolean showDeleteButtons) {
        this.showDeleteButtons = showDeleteButtons;
        notifyDataSetChanged(); // Refresh all items to apply visibility change
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem item = workoutItems.get(position);
        holder.txtExerciseName.setText(item.getExerciseName());
        holder.txtWorkoutDetails.setText(item.toString());
        
        // Show delete button only if enabled for this adapter instance
        holder.btnDelete.setVisibility(showDeleteButtons ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return workoutItems.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName, txtWorkoutDetails;
        ImageView btnDelete;

        public WorkoutViewHolder(@NonNull View itemView, final ItemDeleteListener listener) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
            txtWorkoutDetails = itemView.findViewById(R.id.txtWorkoutDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            
            if (btnDelete != null && listener != null) {
                btnDelete.setOnClickListener(v -> {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemDelete(position);
                    }
                });
            }
        }
    }

    public void addWorkoutItem(WorkoutItem item) {
        workoutItems.add(item);
        notifyItemInserted(workoutItems.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < workoutItems.size()) {
            workoutItems.remove(position);
            notifyItemRemoved(position);
        }
    }
}
