package comp3350.gymbuddy.presentation.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.material.imageview.ShapeableImageView;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.presentation.fragments.AddExerciseBottomSheetFragment;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private final Context context;
    private final List<Exercise> exerciseList;
    private final OnExerciseClickListener clickListener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public ExerciseAdapter(Context context, List<Exercise> exerciseList, OnExerciseClickListener clickListener) {
        this.context = context;
        this.exerciseList = exerciseList;
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
        String imagePath  = "images/" + exercise.getImagePath();
        loadImageFromAssets(imagePath, holder.exerciseImage);

        // Handle View More button click
        holder.viewMoreButton.setOnClickListener(v -> clickListener.onExerciseClick(exercise));
        holder.itemView.setOnClickListener(v -> {
            // Show BottomSheetDialogFragment with the selected exercise
            AddExerciseBottomSheetFragment bottomSheetFragment = new AddExerciseBottomSheetFragment(exercise);
            bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
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

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            viewMoreButton = itemView.findViewById(R.id.exerciseViewMore);
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
}
