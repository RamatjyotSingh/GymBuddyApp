package comp3350.gymbuddy.presentation.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.WorkoutProfile;

public class WorkoutProfileAdapter extends RecyclerView.Adapter<WorkoutProfileAdapter.WorkoutProfileViewHolder> {

    private final List<WorkoutProfile> workoutProfiles;
    private Integer highlightedPosition = null;

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
        holder.txtWorkoutDetails.setText(String.format(Locale.getDefault(), "%d exercises", exerciseCount));

        // Check if this is the highlighted item
        if (highlightedPosition != null && highlightedPosition == position) {
            highlightView(holder.itemView);
        } else {
            // Reset to normal background
            resetHighlight(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return workoutProfiles.size();
    }

    /**
     * Apply highlight animation to a view
     */
    private void highlightView(View itemView) {
        // Try to find a CardView parent if it exists
        CardView cardView = findCardViewParent(itemView);
        
        if (cardView != null) {
            // Animate CardView background
            animateBackground(cardView);
        } else {
            // Animate the item view directly if no CardView
            animateBackground(itemView);
        }
    }
    
    /**
     * Find a CardView parent of a view, if any
     */
    private CardView findCardViewParent(View view) {
        if (view instanceof CardView) {
            return (CardView) view;
        } else if (view.getParent() instanceof CardView) {
            return (CardView) view.getParent();
        }
        return null;
    }

    /**
     * Reset highlight styling
     */
    private void resetHighlight(View itemView) {
        CardView cardView = findCardViewParent(itemView);
        Context context = itemView.getContext();
        
        if (cardView != null) {
            // Use ContextCompat instead of deprecated getColor method
            cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.card_background_normal));
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Animate background color of a view
     */
    private void animateBackground(View view) {
        final Context context = view.getContext();
        int colorTo = ContextCompat.getColor(context, R.color.card_background_highlight);
        int colorFrom;
        final int originalPosition = highlightedPosition;
        
        // Different handling based on view type
        if (view instanceof CardView) {
            CardView cardView = (CardView) view;
            colorFrom = ContextCompat.getColor(context, R.color.card_background_normal);
            
            ValueAnimator colorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), colorFrom, colorTo, colorFrom);
            
            colorAnimation.setDuration(1500); // 1.5 second animation
            colorAnimation.addUpdateListener(animator -> 
                    cardView.setCardBackgroundColor((int) animator.getAnimatedValue()));
            
            colorAnimation.start();
            
            // Reset highlight after animation completes
            colorAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (highlightedPosition != null && highlightedPosition.equals(originalPosition)) {
                        highlightedPosition = null;
                        // Use more specific notification instead of notifyDataSetChanged
                        notifyItemChanged(originalPosition);
                    }
                }
            });
        } else {
            colorFrom = Color.TRANSPARENT;
            
            ValueAnimator colorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), colorFrom, colorTo, colorFrom);
            
            colorAnimation.setDuration(1500);
            colorAnimation.addUpdateListener(animator -> 
                    view.setBackgroundColor((int) animator.getAnimatedValue()));
            
            colorAnimation.start();
            
            colorAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (highlightedPosition != null && highlightedPosition.equals(originalPosition)) {
                        highlightedPosition = null;
                        // Use more specific notification instead of notifyDataSetChanged
                        notifyItemChanged(originalPosition);
                    }
                }
            });
        }
    }

    /**
     * Highlights a specific item in the adapter
     * @param position Position to highlight
     */
    public void highlightItem(int position) {
        if (position >= 0 && position < workoutProfiles.size()) {
            // If there was a previously highlighted item, reset it
            if (highlightedPosition != null) {
                int oldPosition = highlightedPosition;
                highlightedPosition = position;
                notifyItemChanged(oldPosition);
                notifyItemChanged(position);
            } else {
                highlightedPosition = position;
                notifyItemChanged(position);
            }
        }
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

