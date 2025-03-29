package comp3350.gymbuddy.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.activity.StartWorkoutListActivity;

/**
 * Adapter for displaying workout profiles in a RecyclerView
 * Uses ListAdapter for better diffing and animations
 */
public class WorkoutProfileAdapter extends ListAdapter<WorkoutProfile, WorkoutProfileAdapter.WorkoutProfileViewHolder> {
    private boolean showDeleteButtons = false;
    
    // Interface for click callbacks
    public interface OnProfileClickListener {
        void onProfileClicked(WorkoutProfile profile, int position);
    }
    
    // Interface for delete event callbacks
    public interface OnProfileDeleteListener {
        void onProfileDelete(WorkoutProfile profile, int position);
    }
    
    private final OnProfileClickListener clickListener;
    private OnProfileDeleteListener deleteListener;

    // Method to control delete button visibility
    public void setShowDeleteButtons(boolean showDeleteButtons) {
        this.showDeleteButtons = showDeleteButtons;
        notifyDataSetChanged();
    }
    
    // Method to set delete listener
    public void setOnProfileDeleteListener(OnProfileDeleteListener listener) {
        this.deleteListener = listener;
    }

    /**
     * DiffCallback for efficient updates
     */
    private static final DiffUtil.ItemCallback<WorkoutProfile> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull WorkoutProfile oldItem, @NonNull WorkoutProfile newItem) {
                    return oldItem.getID() == newItem.getID();
                }

                @Override
                public boolean areContentsTheSame(@NonNull WorkoutProfile oldItem, @NonNull WorkoutProfile newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                            oldItem.getWorkoutItems().size() == newItem.getWorkoutItems().size();
                }
            };

    /**
     * Constructor with click listener for modern callback approach
     */
    public WorkoutProfileAdapter(@NonNull List<WorkoutProfile> workoutProfiles, OnProfileClickListener listener) {
        super(DIFF_CALLBACK);
        submitList(workoutProfiles);
        this.clickListener = listener;
    }
    
    /**
     * Default constructor that handles navigation internally
     */
    public WorkoutProfileAdapter(@NonNull List<WorkoutProfile> workoutProfiles) {
        this(workoutProfiles, null);
    }

    @NonNull
    @Override
    public WorkoutProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_profile, parent, false);
        return new WorkoutProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutProfileViewHolder holder, int position) {
        WorkoutProfile profile = getItem(position);
        holder.bind(profile, position, clickListener, deleteListener, showDeleteButtons);
    }

    /**
     * ViewHolder for workout profile items
     */
    public class WorkoutProfileViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtWorkoutName;
        private final TextView txtWorkoutDetails;
        private final ImageView btnDeleteProfile;

        public WorkoutProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutDetails = itemView.findViewById(R.id.txtWorkoutDetails);
            btnDeleteProfile = itemView.findViewById(R.id.btnDeleteProfile);
        }
        
        /**
         * Bind data to this view holder
         */
        public void bind(WorkoutProfile profile, int position, 
                        OnProfileClickListener clickListener,
                        OnProfileDeleteListener deleteListener,
                        boolean showDeleteButtons) {
            // Set profile name
            txtWorkoutName.setText(profile.getName());

            // Display exercise count
            int exerciseCount = profile.getWorkoutItems().size();
            txtWorkoutDetails.setText(String.format(Locale.getDefault(), "%d exercises", exerciseCount));
            
            // Set delete button visibility
            btnDeleteProfile.setVisibility(showDeleteButtons ? View.VISIBLE : View.GONE);
            
            // Set delete button click listener
            if (deleteListener != null) {
                btnDeleteProfile.setOnClickListener(v -> 
                    deleteListener.onProfileDelete(profile, getAbsoluteAdapterPosition()));
            }
            
            // Set click listener
            itemView.setOnClickListener(v -> {
                // Use the provided listener if available
                if (clickListener != null) {
                    clickListener.onProfileClicked(profile, position);
                } else {
                    // Default behavior: navigate to StartWorkoutListActivity
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StartWorkoutListActivity.class);
                    intent.putExtra(context.getString(R.string.intent_workout_profile_id), profile.getID());
                    context.startActivity(intent);
                }
            });
        }
    }
}

