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

/**
 * ExerciseHSQLDB implements IExerciseDB, providing methods to interact with the exercise database.
 */
public class ExerciseHSQLDB implements IExerciseDB {

    /**
     * Retrieves all exercises from the database.
     * @return A list of all exercises.
     * @throws DBException If an error occurs while accessing the database.
     */
    @Override
    @NonNull
    public List<Exercise> getAll() throws DBException {
        List<Exercise> exercises = new ArrayList<>();
        String query = "SELECT * FROM exercise";

        try (Connection conn = HSQLDBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and extract exercises
            while (rs.next()) {
                exercises.add(extractExercise(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Failed to load exercises.");
        }

        return exercises;
    }

    /**
     * Retrieves a specific exercise by its ID.
     * @param id The ID of the exercise to retrieve.
     * @return The corresponding Exercise object, or null if not found.
     * @throws DBException If an error occurs while accessing the database.
     */
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

    /**
     * Extracts exercise data from a ResultSet and constructs an Exercise object.
     * @param rs The ResultSet containing the exercise data.
     * @return A new Exercise object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private Exercise extractExercise(ResultSet rs) throws SQLException {
        int exerciseId = rs.getInt("exercise_id");
        String name = rs.getString("name");
        String instructions = rs.getString("instructions");
        String imagePath = rs.getNString("image_path");
        boolean isTimeBased = rs.getBoolean("is_time_based");
        boolean hasWeight = rs.getBoolean("has_weight");

        // Fetch related tags for the exercise
        List<Tag> tags = getTagsByExerciseId(exerciseId);

        return new Exercise(exerciseId, name, tags, instructions, imagePath, isTimeBased, hasWeight);
    }

    /**
     * Extracts tag data from a ResultSet and constructs a Tag object.
     * @param rs The ResultSet containing the tag data.
     * @return A new Tag object populated with the retrieved data.
     * @throws SQLException If an error occurs while reading the result set.
     */
    @NonNull
    private Tag extractTag(ResultSet rs) throws SQLException {
        String name = rs.getString("tag_name");
        int tagType = rs.getInt("tag_type");
        String textColor = rs.getString("text_color");
        String bgColor = rs.getString("background_color");
        Tag.TagType type = Tag.TagType.values()[tagType];

        return new Tag(type, name, textColor, bgColor);
    }

    /**
     * Retrieves a list of tags associated with a given exercise ID.
     * @param exerciseID The ID of the exercise whose tags need to be fetched.
     * @return A list of associated Tag objects.
     * @throws DBException If an error occurs while accessing the database.
     */
    @NonNull
    private List<Tag> getTagsByExerciseId(int exerciseID) throws DBException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT tag_name, tag_type, text_color, background_color " +
                "FROM exercise_tag et " +
                "JOIN tag t ON et.tag_id = t.tag_id " +
                "WHERE et.exercise_id = ?";

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
