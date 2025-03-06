package comp3350.gymbuddy.persistence.hsqldb;

import androidx.annotation.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.interfaces.IExerciseDB;

public class ExerciseHSQLDB implements IExerciseDB {
    @Override
    @NonNull
    public List<Exercise> getAll() throws DBException {
        List<Exercise> exercises = new ArrayList<>();

        String query = "SELECT * FROM exercise";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                exercises.add(extractExercise(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load exercises.");
        }

        return exercises;
    }

    @Override
    public Exercise getExerciseByID(int id) {
        Exercise exercise = null;

        String query = "SELECT * FROM exercise WHERE exercise_id = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exercise = extractExercise(rs);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load exercise.");
        }

        return exercise;
    }

    @NonNull
    private Exercise extractExercise(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        String name = rs.getString("name");
        String instructions = rs.getString("instructions");
        String imagePath = rs.getNString("image_path");
        boolean isTimeBased = rs.getBoolean("is_time_based");
        boolean hasWeight = rs.getBoolean("has_weight");

        // Fetch related tags and instructions
        List<Tag> tags = getTagsByExerciseId(exerciseId);

        return new Exercise(exerciseId, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    @NonNull
    private Tag extractTag(ResultSet rs) throws SQLException {
        String name = rs.getString("tag_name");
        String typeStr = rs.getString("tag_type");
        String textColor = rs.getString("text_color");
        String bgColor = rs.getString("background_color");
        Tag.TagType type = Tag.TagType.valueOf(typeStr);

        return new Tag(type, name, textColor, bgColor);
    }

    @NonNull
    private List<Tag> getTagsByExerciseId(int exerciseID) throws DBException {
        List<Tag> tags = new ArrayList<>();

        String query = "SELECT t.TAG_NAME, t.TAG_TYPE, t.TEXT_COLOR, t.BG_COLOR " +
                "FROM EXERCISE_TAGS lt " +
                "JOIN TAGS t ON lt.TAG_ID = t.TAG_ID " +
                "WHERE lt.EXERCISE_ID = ?";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, exerciseID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(extractTag(rs));
                }
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load exercise tags.");
        }

        return tags;
    }
}
