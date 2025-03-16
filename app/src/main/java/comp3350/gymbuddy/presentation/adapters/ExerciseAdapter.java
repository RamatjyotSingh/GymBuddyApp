package comp3350.gymbuddy.presentation.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.R;
import comp3350.gymbuddy.presentation.util.AssetLoader;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private final ExerciseClickListener clickListener;
    private final ExerciseClickListener viewMoreClickListener;

    public interface ExerciseClickListener {
        void onClickExercise(Exercise exercise);
    }

    public ExerciseAdapter(List<Exercise> exercises, ExerciseClickListener clickListener, ExerciseClickListener viewMoreClickListener) {
        this.exercises = new ArrayList<>(exercises);
        this.clickListener = clickListener;
        this.viewMoreClickListener = viewMoreClickListener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position), clickListener, viewMoreClickListener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = new ArrayList<>(exercises);
        notifyDataSetChanged();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        // Maximum number of tags to be displayed at one time.
        static final int MAX_TAGS = 2;

        private final TextView exerciseName;
        private final ShapeableImageView exerciseImage;
        private final TextView viewMoreButton;
        private final ChipGroup exerciseTags;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            viewMoreButton = itemView.findViewById(R.id.exerciseViewMore);
            exerciseTags = itemView.findViewById(R.id.exerciseTags);
        }

        public void bind(Exercise exercise, ExerciseClickListener clickListener, ExerciseClickListener viewMoreClickListener) {
            // Set name
            exerciseName.setText(exercise.getName());

            // Load image from assets
            var assetLoader = new AssetLoader();
            exerciseImage.setImageBitmap(assetLoader.loadImage(exerciseImage.getContext(), exercise.getImagePath()));

            // Update tags
            bindTags(exercise.getTags());

            // Set click event callbacks
            viewMoreButton.setOnClickListener(v -> viewMoreClickListener.onClickExercise(exercise));
            itemView.setOnClickListener(v -> clickListener.onClickExercise(exercise));
        }

        private void bindTags(List<Tag> tags) {
            // Clear existing chips (tags)
            exerciseTags.removeAllViews();

            // Add tags dynamically (show up to max)
            for (int i = 0; i < Math.min(tags.size(), MAX_TAGS); i++) {
                Tag tag = tags.get(i);
                Chip chip = new Chip(exerciseTags.getContext());
                
                // Set chip content
                chip.setText(tag.getName());
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(tag.getBgColor())));
                chip.setTextColor(Color.parseColor(tag.getTextColor()));
                
                // Disable chip interaction
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setFocusable(false);
                
                //  Remove the ripple effect
                chip.setRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
                

                
                exerciseTags.addView(chip);
            }
        }
    }
}
