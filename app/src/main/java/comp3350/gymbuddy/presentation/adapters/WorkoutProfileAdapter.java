package comp3350.gymbuddy.presentation.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.activity.StartWorkoutListActivity;

/**
 * Adapter for displaying workout profiles in a RecyclerView
 * Uses ListAdapter for better diffing and animations
 */
public class WorkoutProfileAdapter extends ListAdapter<WorkoutProfile, WorkoutProfileAdapter.WorkoutProfileViewHolder> {
    private static final String EXTRA_WORKOUT_PROFILE_ID = "workoutProfileId";
    private Integer highlightedPosition = null;
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
            new DiffUtil.ItemCallback<WorkoutProfile>() {
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
        holder.bind(profile, position, clickListener, this::highlightItem, deleteListener, showDeleteButtons);
    }

    /**
     * Highlights a specific item in the adapter
     * @param position Position to highlight
     */
    public void highlightItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            // If there was a previously highlighted item, reset it
            Integer oldPosition = highlightedPosition;
            highlightedPosition = position;
            
            if (oldPosition != null) {
                notifyItemChanged(oldPosition);
            }
            notifyItemChanged(position);
        }
    }

    /**
     * Clear any highlighted item
     */
    public void clearHighlight() {
        // Store current highlight before clearing
        Integer oldPosition = highlightedPosition;
        
        // Clear highlight
        highlightedPosition = null;
        
        // Update previously highlighted item if there was one
        if (oldPosition != null) {
            notifyItemChanged(oldPosition);
        }
    }

    /**
     * Check if a position is currently highlighted
     */
    public boolean isHighlighted(int position) {
        return highlightedPosition != null && highlightedPosition == position;
    }

    /**
     * ViewHolder for workout profile items
     */
    public class WorkoutProfileViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtWorkoutName;
        private final TextView txtWorkoutDetails;
        private final CardView cardView;
        private final ImageView btnDeleteProfile;

        public WorkoutProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutDetails = itemView.findViewById(R.id.txtWorkoutDetails);
            btnDeleteProfile = itemView.findViewById(R.id.btnDeleteProfile);
            
            // Find CardView if it exists in hierarchy
            CardView foundCardView = null;
            if (itemView instanceof CardView) {
                foundCardView = (CardView) itemView;
            } else if (itemView.getParent() instanceof CardView) {
                foundCardView = (CardView) itemView.getParent();
            }
            cardView = foundCardView;
        }
        
        /**
         * Bind data to this view holder
         */
        public void bind(WorkoutProfile profile, int position, 
                        OnProfileClickListener clickListener,
                        HighlightCallback highlightCallback,
                        OnProfileDeleteListener deleteListener,
                        boolean showDeleteButtons) {
            // Set profile name
            txtWorkoutName.setText(profile.getName());

            // Display exercise count
            int exerciseCount = profile.getWorkoutItems().size();
            txtWorkoutDetails.setText(String.format(Locale.getDefault(), "%d exercises", exerciseCount));

            // Handle highlighting
            if (isHighlighted(position)) {
                applyHighlight();
            } else {
                resetHighlight();
            }
            
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
                    intent.putExtra(EXTRA_WORKOUT_PROFILE_ID, profile.getID());
                    context.startActivity(intent);
                }
                
                // Highlight the clicked item
                if (highlightCallback != null) {
                    highlightCallback.onHighlight(position);
                }
            });
        }
        
        /**
         * Apply highlight styling to this item
         */
        private void applyHighlight() {
            Context context = itemView.getContext();
            int highlightColor = ContextCompat.getColor(context, R.color.card_background_highlight);
            int normalColor = ContextCompat.getColor(context, R.color.card_background_normal);
            
            if (cardView != null) {
                animateCardBackground(cardView, normalColor, highlightColor, normalColor);
            } else {
                animateViewBackground(itemView, Color.TRANSPARENT, highlightColor, Color.TRANSPARENT);
            }
        }
        
        /**
         * Reset highlight styling
         */
        private void resetHighlight() {
            Context context = itemView.getContext();
            
            if (cardView != null) {
                cardView.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.card_background_normal));
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        
        /**
         * Animate CardView background color
         */
        private void animateCardBackground(CardView card, int colorFrom, int colorTo, int colorBack) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), colorFrom, colorTo, colorBack);
            
            colorAnimation.setDuration(1500); // 1.5 second animation
            colorAnimation.addUpdateListener(animator -> 
                    card.setCardBackgroundColor((int) animator.getAnimatedValue()));
            
            colorAnimation.start();
        }
        
        /**
         * Animate View background color
         */
        private void animateViewBackground(View view, int colorFrom, int colorTo, int colorBack) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), colorFrom, colorTo, colorBack);
            
            colorAnimation.setDuration(1500);
            colorAnimation.addUpdateListener(animator -> 
                    view.setBackgroundColor((int) animator.getAnimatedValue()));
            
            colorAnimation.start();
        }
    }
    
    /**
     * Callback for highlight events
     */
    private interface HighlightCallback {
        void onHighlight(int position);
    }
}

