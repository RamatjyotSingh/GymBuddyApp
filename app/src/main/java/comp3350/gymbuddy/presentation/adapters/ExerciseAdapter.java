package comp3350.gymbuddy.presentation.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.presentation.fragments.AddExerciseBottomSheetFragment;
import comp3350.gymbuddy.R;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private final Context context;
    private final List<Exercise> exerciseList;
    private final List<Exercise> fullExerciseList;
    private final OnExerciseClickListener clickListener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public ExerciseAdapter(Context context, List<Exercise> exerciseList, OnExerciseClickListener clickListener) {
        this.context = context;
        this.exerciseList = new ArrayList<>(exerciseList);
        this.fullExerciseList = new ArrayList<>(exerciseList);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseName.setText(exercise.getName());

        // Load image from assets
        String imagePath = "images/" + exercise.getImagePath();
        loadImage(imagePath, holder.exerciseImage);

        // Clear existing chips (tags)
        holder.exerciseTags.removeAllViews();

        // Add tags dynamically (show up to two)
        List<Tag> tags = exercise.getTags();
        for (int i = 0; i < Math.min(tags.size(), 2); i++) {
            Tag tag = tags.get(i);
            Chip chip = new Chip(context);

            setTagStyle(tag, chip);



            holder.exerciseTags.addView(chip);
        }




        holder.viewMoreButton.setOnClickListener(v -> clickListener.onExerciseClick(exercise));
        holder.itemView.setOnClickListener(v -> {
            AddExerciseBottomSheetFragment bottomSheetFragment = new AddExerciseBottomSheetFragment(exercise);
            bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    private void setTagStyle(Tag tag, Chip chip) {

        String name = tag.getName();
        String textColor = tag.getTextColor();
        String backgroundColor = tag.getBgColor();


        chip.setText(name);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(backgroundColor)));
        chip.setTextColor(Color.parseColor(textColor));
    }
    private void loadImage(String imagePath, ShapeableImageView imageView) {
        if (imagePath.startsWith("http")) {
            loadImageFromURL(imagePath, imageView);
        } else {
            loadImageFromAssets(imagePath, imageView);
        }
    }

    private void loadImageFromURL(String imageUrl, ShapeableImageView imageView) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                handler.post(() -> imageView.setImageBitmap(bitmap));
                inputStream.close();
            } catch (Exception e) {
                Log.e("loadImageFromURL", "Error: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        ShapeableImageView exerciseImage;
        TextView viewMoreButton;
        TextView tvMoreTags;
        ChipGroup exerciseTags;



        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            viewMoreButton = itemView.findViewById(R.id.exerciseViewMore);
            tvMoreTags = itemView.findViewById(R.id.tvMoreTags);
            exerciseTags = itemView.findViewById(R.id.exerciseTags);
        }
    }

    private void loadImageFromAssets(String imagePath, ShapeableImageView imageView) {
        try {
            InputStream inputStream = context.getAssets().open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException e) {
            Log.e( "loadImageFromAssets: ", Objects.requireNonNull(e.getMessage()));
        }
    }
    public void filter(String query) {
        // send filter request down to DB layer
        List<Exercise> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(fullExerciseList);
        } else {
            String lowerCaseQuery = query.toLowerCase();

            // go through all exercises
            for (Exercise exercise : fullExerciseList) {
                // check if the exercise name contains the query
                if (exercise.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(exercise);
                } else {
                    // this was recommended to change to something more efficient
                    // otherwise look through the exercise's tags for the query
                    for (Tag tag : exercise.getTags()) {
                        if (tag.getName().toLowerCase().contains(lowerCaseQuery)) {
                            filteredList.add(exercise);
                            break;
                        }
                    }
                }
            }
        }

        if (!filteredList.equals(exerciseList)) {
            exerciseList.clear();
            exerciseList.addAll(filteredList);
            notifyDataSetChanged();
        }
    }
}
